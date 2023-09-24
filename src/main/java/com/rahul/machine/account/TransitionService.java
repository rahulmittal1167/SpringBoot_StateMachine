package com.rahul.machine.account;

import java.util.List;

/**
 * Service which will have method to transition the account
 * It will accept the account Id, transition name
 * Second Method will list all the possible transition
 * @param <T>
 */
public interface TransitionService <T>{

    T transition(Long id, String transition);

    public List<String> getAllowedTransitions(Long id);

}
