package de.othr.hwwa.service;

public interface TwoFactorServiceI {

    public String generateSecret();
    public byte[] generateQrCode();
    public void setup2FA();
    public void verify2FA(String secret);
    public void complete2FASetup(String secret);
}
