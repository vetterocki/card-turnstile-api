package org.example.exception;

import java.math.BigDecimal;

public class InsufficientFundsOnCardBalanceException extends RuntimeException {
  public InsufficientFundsOnCardBalanceException(BigDecimal cardBalance, BigDecimal price) {
    super(String.format("Not enough funds to pay %s, current balance: %s", price, cardBalance));
  }
}
