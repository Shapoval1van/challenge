package com.dws.challenge.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class Account {

  @NotNull
  @NotEmpty
  private final String accountId;

  @NotNull
  @Min(value = 0, message = "Initial balance must be positive.")
  private final BigDecimal balance;

  @NotNull
  @NotEmpty
  private final int version;

  @JsonCreator
  public Account(
      @JsonProperty("accountId") String accountId,
      @JsonProperty("balance") BigDecimal balance,
      @JsonProperty("balance") int version
  ) {
    this.accountId = accountId;
    this.balance = balance;
    this.version = version;
  }

  public Account(Account account) {
    this.accountId = account.accountId;
    this.balance = account.balance;
    this.version = account.version;
  }
}
