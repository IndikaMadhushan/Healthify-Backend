package com.healthcare.personal_health_monitoring.util;

import com.healthcare.personal_health_monitoring.config.ApplicationContextProvider;
import org.springframework.core.env.Environment;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public final class SensitiveDataSupport {

    private static final String ENCRYPTION_PREFIX = "enc:v1:";
    private static final int GCM_TAG_LENGTH_BITS = 128;
    private static final int GCM_IV_LENGTH_BYTES = 12;

    private static volatile KeyMaterial keyMaterial;

    private SensitiveDataSupport() {
    }

    public static String encrypt(String plainText) {
        if (plainText == null) {
            return null;
        }
        if (plainText.startsWith(ENCRYPTION_PREFIX)) {
            return plainText;
        }

        try {
            byte[] iv = new byte[GCM_IV_LENGTH_BYTES];
            SecureRandomHolder.INSTANCE.nextBytes(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(
                    Cipher.ENCRYPT_MODE,
                    new SecretKeySpec(getKeyMaterial().encryptionKey(), "AES"),
                    new GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv)
            );

            byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            byte[] payload = ByteBuffer.allocate(iv.length + cipherText.length)
                    .put(iv)
                    .put(cipherText)
                    .array();

            return ENCRYPTION_PREFIX + Base64.getEncoder().encodeToString(payload);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to encrypt sensitive data", ex);
        }
    }

    public static String decrypt(String storedValue) {
        if (storedValue == null) {
            return null;
        }
        if (!storedValue.startsWith(ENCRYPTION_PREFIX)) {
            return storedValue;
        }

        try {
            byte[] payload = Base64.getDecoder().decode(storedValue.substring(ENCRYPTION_PREFIX.length()));
            ByteBuffer buffer = ByteBuffer.wrap(payload);

            byte[] iv = new byte[GCM_IV_LENGTH_BYTES];
            buffer.get(iv);

            byte[] cipherText = new byte[buffer.remaining()];
            buffer.get(cipherText);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(
                    Cipher.DECRYPT_MODE,
                    new SecretKeySpec(getKeyMaterial().encryptionKey(), "AES"),
                    new GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv)
            );

            return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to decrypt sensitive data", ex);
        }
    }

    public static String blindIndex(String value) {
        String normalized = normalizeForIndex(value);
        if (normalized == null) {
            return null;
        }

        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(getKeyMaterial().blindIndexKey(), "HmacSHA256"));
            byte[] digest = mac.doFinal(normalized.getBytes(StandardCharsets.UTF_8));
            return Hex.encode(digest);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to create blind index", ex);
        }
    }

    private static String normalizeForIndex(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            return null;
        }

        return trimmed.toLowerCase();
    }

    private static KeyMaterial getKeyMaterial() {
        KeyMaterial local = keyMaterial;
        if (local != null) {
            return local;
        }

        synchronized (SensitiveDataSupport.class) {
            if (keyMaterial == null) {
                String secret = resolveSecret();
                if (secret == null || secret.isBlank()) {
                    throw new IllegalStateException(
                            "DATA_ENCRYPTION_KEY must be configured before using sensitive field encryption"
                    );
                }

                keyMaterial = new KeyMaterial(
                        sha256("enc:" + secret),
                        sha256("idx:" + secret)
                );
            }
            return keyMaterial;
        }
    }

    private static String resolveSecret() {
        Environment environment = ApplicationContextProvider.getEnvironment();
        if (environment != null) {
            String property = environment.getProperty("data.encryption.key");
            if (property != null && !property.isBlank()) {
                return property;
            }
        }

        String systemProperty = System.getProperty("data.encryption.key");
        if (systemProperty != null && !systemProperty.isBlank()) {
            return systemProperty;
        }

        String envProperty = System.getenv("DATA_ENCRYPTION_KEY");
        if (envProperty != null && !envProperty.isBlank()) {
            return envProperty;
        }

        return null;
    }

    private static byte[] sha256(String input) {
        try {
            return MessageDigest.getInstance("SHA-256")
                    .digest(input.getBytes(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to derive encryption key material", ex);
        }
    }

    private record KeyMaterial(byte[] encryptionKey, byte[] blindIndexKey) {
    }

    private static final class SecureRandomHolder {
        private static final SecureRandom INSTANCE = new SecureRandom();
    }

    private static final class Hex {
        private static String encode(byte[] value) {
            StringBuilder builder = new StringBuilder(value.length * 2);
            for (byte current : value) {
                builder.append(Character.forDigit((current >> 4) & 0xF, 16));
                builder.append(Character.forDigit(current & 0xF, 16));
            }
            return builder.toString();
        }
    }
}
