
package com.dws.challenge.exception;

public class AccountNotFoundException extends RuntimeException {

  public AccountNotFoundException(String id) {
    super(String.format("Account with id: %s not found", id));
  }
}
