package com.rahul.machine.account.transitions;

import com.rahul.machine.account.AccountDTO;
import com.rahul.machine.account.AccountStatus;
import com.rahul.machine.account.Transition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Approve implements Transition<AccountDTO> {

    public static final String NAME = "approve";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public AccountStatus getTargetStatus() {
        return AccountStatus.APPROVED;
    }

    @Override
    public void applyProcessing(AccountDTO order) {
        log.info("Account is transitioning to Approved State {}", order.getId());
    }
}
