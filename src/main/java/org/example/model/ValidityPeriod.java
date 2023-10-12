package org.example.model;

import java.time.LocalDateTime;

public enum ValidityPeriod {
  TEN_DAYS(10), MONTH(30);

  final LocalDateTime dateTime;

  ValidityPeriod(int daysFromNow) {
    this.dateTime = LocalDateTime.now().plusDays(daysFromNow);
  }

  public LocalDateTime getDateTime() {
    return dateTime;
  }
}
