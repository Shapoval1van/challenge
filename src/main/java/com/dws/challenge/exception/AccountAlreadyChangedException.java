package com.dws.challenge.exception;

public class AccountAlreadyChangedException extends RuntimeException {

  public AccountAlreadyChangedException(String id) {
    super(String.format("Account with id: %s was changes please retry operation", id));
  }
}
