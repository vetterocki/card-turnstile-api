package org.example.model.card;

public enum TravelAmount {
  FIVE(5), TEN(10);

  final int value;

  TravelAmount(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
