package de.othr.hwwa.service.impl;

import de.othr.hwwa.exceptions.TwoFactorAuthentificationException;
import de.othr.hwwa.model.User;
import de.othr.hwwa.repository.UserRepositoryI;
import de.othr.hwwa.service.TwoFactorServiceI;
import dev.samstevens.totp.code.*;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import org.springframework.stereotype.Service;

@Service
public class TwoFactorServiceImpl extends SecurityServiceImpl implements TwoFactorServiceI {

    private final UserRepositoryI userRepository;
    private final SecretGenerator secretGenerator = new DefaultSecretGenerator();
    private final QrGenerator qrGenerator = new ZxingPngQrGenerator();
    private final CodeVerifier codeVerifier;

    public TwoFactorServiceImpl(UserRepositoryI userRepository) {
        this.userRepository = userRepository;

        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        DefaultCodeVerifier verifier =
                new DefaultCodeVerifier(codeGenerator, timeProvider);
        verifier.setAllowedTimePeriodDiscrepancy(1);
        this.codeVerifier = verifier;
    }

    public String generateSecret() {
        return secretGenerator.generate();
    }

    public byte[] generateQrCode() {
        User currentUser = getCurrentUser();
        String email = currentUser.getEmail();
        String secret = currentUser.getTwoFactorSecret();
        if (secret == null) {
            throw new IllegalStateException("secret was not initialized yet");
        }
        QrData data = new QrData.Builder()
                .label(email)
                .secret(secret)
                .issuer("CraftManager")
                .algorithm(HashingAlgorithm.SHA1)
                .digits(6)
                .period(30)
                .build();

        try {
            return qrGenerator.generate(data);
        } catch (Exception e) {
            throw new RuntimeException("QR generation failed", e);
        }
    }

    public void setup2FA() {
        User user = getCurrentUser();
        if (user.isTwoFactorEnabled()){
            throw new IllegalStateException("2 Faktor Authentifizierung ist bereits angelegt");
        }
        String secret = generateSecret();
        user.setTwoFactorSecret(secret);
        userRepository.save(user);
    }

    public void complete2FASetup(String secret) {
        User user = getCurrentUser();
        String twoFactorSecret = user.getTwoFactorSecret();
        if (!codeVerifier.isValidCode(twoFactorSecret, secret)) {
            throw new TwoFactorAuthentificationException("Ungültiger Code");
        }
        user.setTwoFactorEnabled(true);
        userRepository.save(user);
    }

    public void verify2FA(String secret) {
        User user = getCurrentUser();
        String twoFactorSecret = user.getTwoFactorSecret();
        if (!codeVerifier.isValidCode(twoFactorSecret, secret)) {
            throw new TwoFactorAuthentificationException("Ungültiger Code");
        }
    }
}
