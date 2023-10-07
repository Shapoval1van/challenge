package com.dws.challenge.service;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.AccountAlreadyChangedException;
import com.dws.challenge.exception.AccountNotFoundException;
import com.dws.challenge.repository.AccountsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@AllArgsConstructor
@Service
public class TransferServiceImpl implements TransferService {

  private final AccountsRepository accountsRepository;
  private final TransferValidationService transferValidationService;
  private final NotificationService notificationService;


  /*
  operation1 A -> B           operation2 B -> C
  A change amount from
  |                           B change amount from
  |                           |
  |                           |
  |                           B save
  A save
                              C change amount to
  B change amount to          |
  |                           |
  |                           C save
  |
  |
  B save -> optimistic lock exception

  then A amount should be rollback
 */
  @Override
  public void transfer(String accountFromId, String accountToId, BigDecimal amount) {
    Account to = accountsRepository.getAccount(accountToId)
        .orElseThrow(() -> new AccountNotFoundException(accountToId));
    Account from = accountsRepository.getAccount(accountFromId)
        .orElseThrow(() -> new AccountNotFoundException(accountFromId));

    transferValidationService.validateTransfer(from, to, amount);

    try {
      accountsRepository.updateAccount(
          accountFromId,
          new Account(
              from.getAccountId(),
              from.getBalance().subtract(amount),
              from.getVersion() + 1
          )
      );
      accountsRepository.updateAccount(
          accountToId,
          new Account(
              to.getAccountId(),
              to.getBalance().add(amount),
              to.getVersion() + 1
          )
      );
    } catch (AccountAlreadyChangedException e) {
      log.error("Race condition detected please reinstall the Windows");
      //here I should do some rollback, but it is not technically possible since I do not know what the previous value of balance
      //I can reduce probability of race condition as mush as possible if updateAccount return previous balance
      //but it also not a bulletproof solution
      //I will share another method in the README.md
      throw e;
    }
    notificationService.notifyAboutTransfer(
        to,
        "Dude someone send money for you, please check it. And do not forget to pay a tax"
    );
  }

}
