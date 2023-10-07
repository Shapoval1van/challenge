package com.dws.challenge.service;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.TransferNotValidException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransferValidationServiceImpl implements TransferValidationService{
  @Override
  public void validateTransfer(Account from, Account to, BigDecimal amount) {
    if(from.getBalance().compareTo(amount) < 0) {
      throw new TransferNotValidException("Account with id " + from.getAccountId() + "does not have needed amount");
    }
  }
}
