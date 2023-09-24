package com.rahul.machine.handler;

import com.rahul.machine.event.AccountTransitionedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AccountSubmittedEventHandler implements ApplicationListener<AccountTransitionedEvent> {
    @Override
    public void onApplicationEvent(AccountTransitionedEvent event) {
        log.info("Received account status changed event for account {} from state {} to this state {}  from source {}",
                event.getAccountDTO().getId(), event.accountStatus(),
                event.getAccountDTO().getStatus(), event.getSource());
    }
}
