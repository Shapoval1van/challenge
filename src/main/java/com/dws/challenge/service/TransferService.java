package com.dws.challenge.service;

import java.math.BigDecimal;

public interface TransferService {
  /**
   * Provide money transfer between account
   * @param accountFromId account id from which the withdrawal will take place
   * @param accountToId account id what will receive amount of transfer
   * @param amount amount to transfer
   */
  void transfer(String accountFromId, String accountToId, BigDecimal amount);
}
