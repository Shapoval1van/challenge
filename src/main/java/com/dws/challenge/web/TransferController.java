
package com.dws.challenge.web;

import com.dws.challenge.exception.AccountAlreadyChangedException;
import com.dws.challenge.exception.AccountNotFoundException;
import com.dws.challenge.exception.TransferNotValidException;
import com.dws.challenge.service.TransferService;
import com.dws.challenge.web.dto.TransferRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/transfer")
@Slf4j
public class TransferController {

  private final TransferService accountsService;

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> createAccount(@RequestBody @Valid TransferRequest account) {
    accountsService.transfer(account.getAccountFromId(), account.getAccountToId(), account.getAmount());
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @ExceptionHandler({AccountAlreadyChangedException.class})
  public ResponseEntity<String> handleOptimisticLock(AccountAlreadyChangedException exception) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
  }

  @ExceptionHandler({AccountNotFoundException.class})
  public ResponseEntity<String> handleAccountNotFount(AccountNotFoundException exception) {
    //here account not a REST resource so I decided to return bad request interested of not found
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler({TransferNotValidException.class})
  public ResponseEntity<String> handleValidation(TransferNotValidException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }
}

