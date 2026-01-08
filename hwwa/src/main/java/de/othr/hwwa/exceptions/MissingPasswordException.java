package de.othr.hwwa.exceptions;

public class MissingPasswordException extends RuntimeException {
    public MissingPasswordException(String message) {
        super(message);
    }
}
