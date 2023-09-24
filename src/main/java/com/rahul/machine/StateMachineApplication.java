package com.rahul.machine;

import com.rahul.machine.account.AccountDTO;
import com.rahul.machine.account.AccountStatus;
import com.rahul.machine.account.TransitionService;
import com.rahul.machine.account.transitions.Approve;
import com.rahul.machine.account.transitions.Notify;
import com.rahul.machine.account.transitions.Reject;
import com.rahul.machine.account.transitions.Submit;
import com.rahul.machine.model.Account;
import com.rahul.machine.repo.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class StateMachineApplication implements CommandLineRunner {


	private final TransitionService<AccountDTO> accountTransitionService;
	private final AccountRepository accountRepository;


	public static void main(String[] args) {
		SpringApplication.run(StateMachineApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Account account = new Account();
		account.setStatus(AccountStatus.DRAFT);
		accountRepository.save(account);
		accountTransitionService.getAllowedTransitions(account.getId())
				.stream()
				.forEach(System.out::println);

		//Draft -> Submitted
		accountTransitionService.transition(account.getId(), Submit.NAME);

		//IN_REVIEW
		accountTransitionService.transition(account.getId(), Reject.NAME);

		//Draft -> Submitted
		accountTransitionService.transition(account.getId(), Submit.NAME);

		//InReview -> Approved
		accountTransitionService.transition(account.getId(), Approve.NAME);

		//Approve -> Approve
		accountTransitionService.transition(account.getId(), Approve.NAME);

		//Approve -> Notify
		accountTransitionService.transition(account.getId(), Notify.NAME);
	}

}
