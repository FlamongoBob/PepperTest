package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import androidx.lifecycle.LifecycleOwner;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.example.myapplication.fragments.SplashImageFragment;
import com.example.myapplication.fragments.StateListFragment;
import com.example.myapplication.fragments.StateMachineWatcher;
import com.example.myapplication.statemachine.InteractionStateMachine;
import com.example.myapplication.utils.Manager;

import java.util.ArrayList;

public class MainActivity extends RobotActivity implements RobotLifecycleCallbacks {
    String TAG = "MainActivity";
    QiContext qiContext;
    InteractionStateMachine stateMachine;
    Manager mgr = new Manager(this);

    ///////////////////////////////////
    // Android lifecycle callbacks
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        QiSDK.register(this, this);

       // frgMng.beginTransaction().add(R.id.container, frgServer, "frgServer").hide(frgServer).commit();
       // frgMng.beginTransaction().add(R.id.container, frgPatient, "frgPatient").hide(frgPatient).commit();
    }

    @Override
    protected void onDestroy() {
        QiSDK.unregister(this, this);
        super.onDestroy();
    }

    ///////////////////////////////////
    // Robot lifecycle callbacks
    Fragment lastFragment = null;


    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        Log.i(TAG, "Robot Focus gained");
        this.qiContext = qiContext;
        stateMachine = new InteractionStateMachine(qiContext, getApplicationContext());
        for (StateMachineWatcher watcher : stateMachineWatchers) {
            runOnUiThread(() -> watcher.onStateMachineReady(stateMachine));
        }
        runOnUiThread(() -> stateMachine.fragment.observe((LifecycleOwner) this, this::setFragment));
        stateMachine.start();
    }

    private void setFragment(Fragment fragment) {
        runOnUiThread(() -> {
            if (fragment != null) {
                mgr.replaceFragment(fragment);
               // frgMng.beginTransaction().replace(R.id.stateFragmentHolder, fragment).commit();
            } else if (lastFragment != null) {
                mgr.removeLastFragment(lastFragment);
              //  frgMng.beginTransaction().remove(lastFragment).commit();
            }
            lastFragment = fragment;
        });
    }
    
    @Override
    public void onRobotFocusLost() {
        Log.i(TAG, "Robot Focus lost");
        stateMachine.stop();
    }

    @Override
    public void onRobotFocusRefused(String reason) {
        Log.i(TAG, "Robot Focus refused because " + reason);
    }

    ArrayList<StateMachineWatcher> stateMachineWatchers = new ArrayList<>();

    public void addStateMachineWatcher(StateMachineWatcher watcher) {
        stateMachineWatchers.add(watcher);
        if (stateMachine != null) {
            runOnUiThread(() -> watcher.onStateMachineReady(stateMachine));
        }
    }
}
