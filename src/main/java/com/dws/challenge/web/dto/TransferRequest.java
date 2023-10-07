package com.dws.challenge.web.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor(onConstructor_ = @JsonCreator)
public class TransferRequest {
  @NotNull
  @NotEmpty
  private final String accountFromId;
  @NotNull
  @NotEmpty
  private final String accountToId;
  @NotNull
  @Min(value = 0, message = "Transfer amount must be positive.")
  private final BigDecimal amount;
}
