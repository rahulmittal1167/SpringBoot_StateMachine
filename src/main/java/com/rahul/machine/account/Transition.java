package com.rahul.machine.account;

public interface Transition<T>{

    // Get the Transition
    String getName();

    AccountStatus getTargetStatus();

    // Method which all transition will implement
    void applyProcessing(T order);
}
