package com.rahul.machine.account.transitions;

import com.rahul.machine.account.AccountDTO;
import com.rahul.machine.account.AccountStatus;
import com.rahul.machine.account.Transition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Notify implements Transition<AccountDTO> {

    public static final String NAME = "notify";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public AccountStatus getTargetStatus() {
        return AccountStatus.NOTIFIED;
    }

    @Override
    public void applyProcessing(AccountDTO accountDTO) {
        log.info("Account is transition to notification state {}", accountDTO.getId());
    }
}
