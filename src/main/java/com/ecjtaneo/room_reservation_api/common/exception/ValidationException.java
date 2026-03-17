package com.ecjtaneo.room_reservation_api.common.exception;

public class ValidationException extends RuntimeException {
  public ValidationException(String message) {
    super(message);
  }
}
