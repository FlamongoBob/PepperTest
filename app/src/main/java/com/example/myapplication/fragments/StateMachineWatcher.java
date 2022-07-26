package com.example.myapplication.fragments;


import com.example.myapplication.statemachine.InteractionStateMachine;

/*
 * An interface for those who want to be notified when the app's state machine is ready.
 */
public interface StateMachineWatcher {
    void onStateMachineReady(InteractionStateMachine stateMachine);
}
