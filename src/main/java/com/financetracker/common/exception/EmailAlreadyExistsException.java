// src/main/java/com/financetracker/common/exception/EmailAlreadyExistsException.java
package com.financetracker.common.exception;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
