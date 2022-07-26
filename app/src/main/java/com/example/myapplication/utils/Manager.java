package com.example.myapplication.utils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.fragments.SplashImageFragment;
import com.example.myapplication.fragments.StateListFragment;

public class Manager extends AppCompatActivity {
    MainActivity mainActivity;


    SplashImageFragment frgSplashImage = new SplashImageFragment(1,1);
    StateListFragment frgStateList = new StateListFragment(mainActivity);

    public FragmentManager frgMng = getSupportFragmentManager();
    Fragment activeFragment = frgSplashImage;

    public Manager(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    public void replaceFragment(Fragment fragment){
        frgMng.beginTransaction().replace(R.id.stateFragmentHolder, fragment).commit();
    }

    public void removeLastFragment(Fragment lastFragment){
        frgMng.beginTransaction().remove(lastFragment).commit();
    }




}
