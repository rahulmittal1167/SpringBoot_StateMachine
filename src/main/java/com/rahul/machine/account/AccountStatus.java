package com.rahul.machine.account;

import com.rahul.machine.account.transitions.Approve;
import com.rahul.machine.account.transitions.Notify;
import com.rahul.machine.account.transitions.Reject;
import com.rahul.machine.account.transitions.Submit;

import java.util.List;

public enum AccountStatus {

    // We will declare multiple states here
    // Each State can have multiple transitions
    DRAFT(Submit.NAME),
    IN_REVIEW(Approve.NAME, Reject.NAME),
    APPROVED(Approve.NAME, Notify.NAME),
    NOTIFIED();

    private final List<String> transitions;

    AccountStatus(String... transitions){
        this.transitions= List.of(transitions);
    }

    public List<String> getTransitions(){
        return transitions;
    }
}
