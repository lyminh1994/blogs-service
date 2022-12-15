package com.minhlq.blogsservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * This class holds all constants used in Encryption service.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EncryptionConstants {

    public static final String ENCRYPTING_DATA_ERROR = "Error encrypting data";
    public static final String DECRYPTING_DATA_ERROR = "Error decrypting data";
    public static final String DERIVATION_FUNCTION = "PBKDF2WithHmacSHA256";
    public static final String ENCRYPT_ALGORITHM = "AES/GCM/NoPadding";
    public static final String AES_ALGORITHM = "AES";

    public static final int GCM_TAG_LENGTH = 12; // bits
    public static final int GCM_IV_LENGTH = 12;

    // The password-based key derivation function
    public static final int ITERATION_COUNT = 65536;
    public static final int KEY_LENGTH = 256;
}
