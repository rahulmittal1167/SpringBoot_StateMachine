package com.rahul.machine.account;

import com.rahul.machine.event.AccountTransitionedEvent;
import com.rahul.machine.model.Account;
import com.rahul.machine.repo.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AccountTransitionService implements TransitionService<AccountDTO>{

    private final AccountRepository accountRepository;
    private Map<String, Transition> transitionMap;
    private final List<Transition> transitions;
    private final ModelMapper mapper;
    private final ApplicationEventPublisher applicationEventPublisher;


    /**
     * Transitions the order from the current state to the target state
     *
     * @param id:         the id of the account
     * @param transition: the status to transition to
     * @return: the order details
     */
    @Transactional
    public AccountDTO transition(Long id, String transition) {
        Transition accountTranstition = transitionMap.get(transition.toLowerCase());
        if(accountTranstition == null){
            throw new IllegalArgumentException("UnKnown Transition: " + transition);
        }

        return accountRepository.findById(id)
                .map(order -> {
                    checkAllowed(accountTranstition,order.getStatus());
                    // After apply processing we will map Order to AccountDTO
                    accountTranstition.applyProcessing(mapper.map(order, AccountDTO.class));
                    return updateStatus(order, accountTranstition.getTargetStatus());
                })
                .map(u-> mapper.map(u, AccountDTO.class))
                .orElseThrow(() -> new IllegalArgumentException("Unknown Order : "+ id));
    }

    private Object updateStatus(Account order, AccountStatus accountStatus){
        AccountStatus existingStatus = order.getStatus();
        order.setStatus(accountStatus);

        Account updated = accountRepository.save(order);
        var event = new AccountTransitionedEvent(this, existingStatus, mapper.map(updated, AccountDTO.class));
        applicationEventPublisher.publishEvent(event);
        return updated;
    }

    /**
     * Returns a list of transitions allowed for a particular order identified by the id
     *
     * @param id: the id  of the order
     * @return: list of  transitions allowed
     */
    @Override
    public List<String> getAllowedTransitions(Long id) {
        Account account =
                accountRepository.findById(id)
                        .orElseThrow(()-> new IllegalArgumentException("Unknown account: " + id));

        return account.getStatus().getTransitions();
    }

    @PostConstruct
    private void initTransitions(){
        Map<String, Transition> transitionHashMap = new HashMap<>();

        for(Transition accountTransition : transitions){
            if(transitionHashMap.containsKey(accountTransition.getName())){
                throw new IllegalArgumentException("Duplicate transition .." + accountTransition.getName());
            }

            transitionHashMap.put(accountTransition.getName(), accountTransition);
        }
        this.transitionMap = transitionHashMap;
    }

    private void checkAllowed(Transition accountTransition, AccountStatus status){
        Set<AccountStatus> allowedSourceStatus = Stream.of(AccountStatus.values())
                .filter(s -> s.getTransitions().contains(accountTransition.getName()))
                .collect(Collectors.toSet());

        if(!allowedSourceStatus.contains(status)){
            throw new RuntimeException("The transition from current state " + status.name() + " to the target state "
                + accountTransition.getTargetStatus().name() + " is not allowed");
        }
    }

}
