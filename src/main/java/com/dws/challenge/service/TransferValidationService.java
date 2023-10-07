package com.dws.challenge.service;

import com.dws.challenge.domain.Account;

import java.math.BigDecimal;

/**
 * Service responsible to do any possible validation for transfer
 * since transfer politics can be quite wide we should move it to another interface
 * P.S Also according single responsibilities principle
 */
public interface TransferValidationService {
  void validateTransfer(Account from, Account to, BigDecimal amount);
}
