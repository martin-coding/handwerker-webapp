package de.othr.hwwa.exceptions;

public class TwoFactorAuthentificationException extends RuntimeException {
    public TwoFactorAuthentificationException(String message) {
        super(message);
    }
}
