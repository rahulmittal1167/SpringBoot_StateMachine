package com.rahul.machine.event;

import com.rahul.machine.account.AccountDTO;
import com.rahul.machine.account.AccountStatus;
import org.springframework.context.ApplicationEvent;

public class AccountTransitionedEvent extends ApplicationEvent {

    private final AccountDTO accountDTO;
    private final AccountStatus accountStatus;
    public AccountTransitionedEvent(Object source, AccountStatus status, AccountDTO accountDTO) {
        super(source);
        this.accountDTO = accountDTO;
        this.accountStatus = status;
    }

    public AccountDTO getAccountDTO(){
        return this.accountDTO;
    }

    public AccountStatus accountStatus(){
        return this.accountStatus;
    }
}
