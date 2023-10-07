package com.dws.challenge.service;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.AccountNotFoundException;
import com.dws.challenge.exception.TransferNotValidException;
import com.dws.challenge.repository.AccountsRepository;
import com.dws.challenge.repository.AccountsRepositoryInMemory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TransferServiceImplTest {

  private AccountsRepository accountsRepository = new AccountsRepositoryInMemory();
  private TransferValidationService transferValidationService = new TransferValidationServiceImpl();
  private NotificationService notificationService = mock(NotificationService.class);

  private TransferService transferService;

  @BeforeEach
  public void init() {
    accountsRepository.clearAccounts();
    transferService = new TransferServiceImpl(accountsRepository, transferValidationService, notificationService);
  }

  @Test
  public void testTransfer() {
    //given
    accountsRepository.createAccount(new Account("1", new BigDecimal(10), 0));
    accountsRepository.createAccount(new Account("2", new BigDecimal(0), 1));

    //when
    transferService.transfer("1", "2", new BigDecimal(5));
    //then

    assertThat(accountsRepository.getAccount("1")).isNotEmpty();
    assertThat(accountsRepository.getAccount("1").get().getVersion()).isEqualTo(1);
    assertThat(accountsRepository.getAccount("1").get().getBalance()).isEqualTo(new BigDecimal(5));

    assertThat(accountsRepository.getAccount("2")).isNotEmpty();
    assertThat(accountsRepository.getAccount("2").get().getVersion()).isEqualTo(2);
    assertThat(accountsRepository.getAccount("2").get().getBalance()).isEqualTo(new BigDecimal(5));

    verify(notificationService).notifyAboutTransfer(any(Account.class), any(String.class));
  }

  @Test
  public void testTransfer_accountNotFound() {
    //given
    accountsRepository.createAccount(new Account("1", new BigDecimal(10), 0));
    accountsRepository.createAccount(new Account("2", new BigDecimal(0), 1));

    //when
    assertThrows(AccountNotFoundException.class, () -> transferService.transfer("1", "3", new BigDecimal(5)));
    verify(notificationService, never()).notifyAboutTransfer(any(Account.class), any(String.class));
  }


  @Test
  public void testTransfer_TransferNotValid() {
    //given
    accountsRepository.createAccount(new Account("1", new BigDecimal(10), 0));
    accountsRepository.createAccount(new Account("2", new BigDecimal(0), 1));

    //when
    assertThrows(TransferNotValidException.class, () -> transferService.transfer("1", "2", new BigDecimal(35)));
    verify(notificationService, never()).notifyAboutTransfer(any(Account.class), any(String.class));
  }
}