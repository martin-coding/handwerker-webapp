package de.othr.hwwa.exceptions;

public class InvoiceDoesNotExistException extends RuntimeException {
    public InvoiceDoesNotExistException(String message) {
        super(message);
    }
}
