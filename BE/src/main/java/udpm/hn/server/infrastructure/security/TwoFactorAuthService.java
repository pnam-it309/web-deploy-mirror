package udpm.hn.server.infrastructure.security;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import dev.samstevens.totp.code.*;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import udpm.hn.server.entity.Admin;
import udpm.hn.server.entity.TwoFactorAuth;
import udpm.hn.server.repository.TwoFactorAuthRepository;

import java.io.ByteArrayOutputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TwoFactorAuthService {

    private final TwoFactorAuthRepository twoFactorAuthRepository;
    private final DefaultSecretGenerator secretGenerator = new DefaultSecretGenerator();
    private final TimeProvider timeProvider = new SystemTimeProvider();
    private final CodeGenerator codeGenerator = new DefaultCodeGenerator();
    private final CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);

    /**
     * Generate 2FA secret and QR code for admin
     */
    public TwoFactorSetupResponse generateSecret(Admin admin) {
        // Generate secret
        String secret = secretGenerator.generate();

        // Generate QR code data
        String qrCodeData = generateQrCodeDataUri(admin.getUsername(), secret);

        // Generate backup codes
        List<String> backupCodes = generateBackupCodes();

        // Save to database (but not enabled yet)
        TwoFactorAuth twoFactorAuth = twoFactorAuthRepository.findByAdminId(admin.getId())
                .orElse(new TwoFactorAuth());

        twoFactorAuth.setAdmin(admin);
        twoFactorAuth.setSecret(secret);
        twoFactorAuth.setBackupCodes(String.join(",", backupCodes));
        twoFactorAuth.setEnabled(false);

        twoFactorAuthRepository.save(twoFactorAuth);

        return new TwoFactorSetupResponse(secret, qrCodeData, backupCodes);
    }

    /**
     * Verify OTP code and enable 2FA
     */
    public boolean verifyAndEnable(String adminId, String code) {
        TwoFactorAuth twoFactorAuth = twoFactorAuthRepository.findByAdminId(adminId)
                .orElseThrow(() -> new RuntimeException("2FA not set up"));

        boolean isValid = verifier.isValidCode(twoFactorAuth.getSecret(), code);

        if (isValid) {
            twoFactorAuth.setEnabled(true);
            twoFactorAuth.setVerifiedAt(System.currentTimeMillis());
            twoFactorAuthRepository.save(twoFactorAuth);
        }

        return isValid;
    }

    /**
     * Verify OTP code during login
     */
    public boolean verifyCode(String adminId, String code) {
        TwoFactorAuth twoFactorAuth = twoFactorAuthRepository.findByAdminId(adminId)
                .orElse(null);

        if (twoFactorAuth == null || !twoFactorAuth.getEnabled()) {
            return true; // 2FA not enabled, allow login
        }

        // Check if it's a backup code
        if (isBackupCode(twoFactorAuth, code)) {
            removeBackupCode(twoFactorAuth, code);
            return true;
        }

        // Verify TOTP code
        return verifier.isValidCode(twoFactorAuth.getSecret(), code);
    }

    /**
     * Disable 2FA for admin
     */
    public void disable(String adminId) {
        twoFactorAuthRepository.findByAdminId(adminId).ifPresent(twoFactorAuth -> {
            twoFactorAuth.setEnabled(false);
            twoFactorAuthRepository.save(twoFactorAuth);
        });
    }

    /**
     * Check if 2FA is enabled for admin
     */
    public boolean isEnabled(String adminId) {
        return twoFactorAuthRepository.findByAdminId(adminId)
                .map(TwoFactorAuth::getEnabled)
                .orElse(false);
    }

    private String generateQrCodeDataUri(String username, String secret) {
        try {
            QrData data = new QrData.Builder()
                    .label(username)
                    .secret(secret)
                    .issuer("FPL UDPM Catalog")
                    .algorithm(HashingAlgorithm.SHA1)
                    .digits(6)
                    .period(30)
                    .build();

            String qrCodeUrl = "otpauth://totp/FPL%20UDPM%20Catalog:" + username +
                    "?secret=" + secret + "&issuer=FPL%20UDPM%20Catalog";

            // Generate QR code image
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeUrl, BarcodeFormat.QR_CODE, 250, 250);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            byte[] qrCodeBytes = outputStream.toByteArray();

            return "data:image/png;base64," + Base64.getEncoder().encodeToString(qrCodeBytes);
        } catch (Exception e) {
            log.error("Failed to generate QR code", e);
            throw new RuntimeException("Failed to generate QR code", e);
        }
    }

    private List<String> generateBackupCodes() {
        List<String> codes = new ArrayList<>();
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < 10; i++) {
            int code = 100000 + random.nextInt(900000); // 6-digit codes
            codes.add(String.valueOf(code));
        }

        return codes;
    }

    private boolean isBackupCode(TwoFactorAuth twoFactorAuth, String code) {
        String backupCodes = twoFactorAuth.getBackupCodes();
        if (backupCodes == null)
            return false;

        return backupCodes.contains(code);
    }

    private void removeBackupCode(TwoFactorAuth twoFactorAuth, String usedCode) {
        String backupCodes = twoFactorAuth.getBackupCodes();
        String[] codes = backupCodes.split(",");
        List<String> remainingCodes = new ArrayList<>();

        for (String code : codes) {
            if (!code.equals(usedCode)) {
                remainingCodes.add(code);
            }
        }

        twoFactorAuth.setBackupCodes(String.join(",", remainingCodes));
        twoFactorAuthRepository.save(twoFactorAuth);
    }

    public static class TwoFactorSetupResponse {
        public final String secret;
        public final String qrCodeDataUri;
        public final List<String> backupCodes;

        public TwoFactorSetupResponse(String secret, String qrCodeDataUri, List<String> backupCodes) {
            this.secret = secret;
            this.qrCodeDataUri = qrCodeDataUri;
            this.backupCodes = backupCodes;
        }
    }
}
