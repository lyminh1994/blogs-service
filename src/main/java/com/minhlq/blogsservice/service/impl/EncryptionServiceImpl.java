package com.minhlq.blogsservice.service.impl;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.minhlq.blogsservice.constant.EncryptionConstants;
import com.minhlq.blogsservice.exception.EncryptionException;
import com.minhlq.blogsservice.service.EncryptionService;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * This is the implementation of the encryption service.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Service
public class EncryptionServiceImpl implements EncryptionService {

  private final SecureRandom random = new SecureRandom();

  @Value("${encryption.secret.password}")
  private String password;

  @Value("${encryption.secret.salt}")
  private String salt;

  @Override
  public String encrypt(String text) {
    try {
      if (StringUtils.isBlank(text)) {
        return null;
      }

      byte[] iv = new byte[EncryptionConstants.GCM_IV_LENGTH];
      random.nextBytes(iv);

      Cipher cipher = Cipher.getInstance(EncryptionConstants.ENCRYPT_ALGORITHM);
      GCMParameterSpec ivSpec =
          new GCMParameterSpec(EncryptionConstants.GCM_TAG_LENGTH * Byte.SIZE, iv);
      cipher.init(Cipher.ENCRYPT_MODE, getKeyFromPassword(), ivSpec);

      byte[] ciphertext = cipher.doFinal(text.getBytes(UTF_8));
      byte[] encrypted = new byte[iv.length + ciphertext.length];
      System.arraycopy(iv, 0, encrypted, 0, iv.length);
      System.arraycopy(ciphertext, 0, encrypted, iv.length, ciphertext.length);

      return Base64.getEncoder().encodeToString(encrypted);
    } catch (NoSuchAlgorithmException
        | IllegalArgumentException
        | InvalidKeyException
        | InvalidAlgorithmParameterException
        | NoSuchPaddingException
        | IllegalBlockSizeException
        | BadPaddingException e) {
      log.debug("Error encrypting data", e);
      throw new EncryptionException(e);
    }
  }

  @Override
  public String decrypt(String encryptedText) {
    try {
      if (StringUtils.isBlank(encryptedText)) {
        return null;
      }

      byte[] decoded = Base64.getDecoder().decode(encryptedText);
      byte[] iv = Arrays.copyOfRange(decoded, 0, EncryptionConstants.GCM_IV_LENGTH);

      Cipher cipher = Cipher.getInstance(EncryptionConstants.ENCRYPT_ALGORITHM);
      GCMParameterSpec ivSpec =
          new GCMParameterSpec(EncryptionConstants.GCM_TAG_LENGTH * Byte.SIZE, iv);
      cipher.init(Cipher.DECRYPT_MODE, getKeyFromPassword(), ivSpec);

      byte[] ciphertext =
          cipher.doFinal(
              decoded,
              EncryptionConstants.GCM_IV_LENGTH,
              decoded.length - EncryptionConstants.GCM_IV_LENGTH);

      return new String(ciphertext, StandardCharsets.UTF_8);
    } catch (NoSuchAlgorithmException
        | IllegalArgumentException
        | InvalidKeyException
        | InvalidAlgorithmParameterException
        | IllegalBlockSizeException
        | BadPaddingException
        | NoSuchPaddingException e) {
      log.debug("Error decrypting data", e);
      throw new EncryptionException(e);
    }
  }

  @Override
  public String encode(String text) {
    if (StringUtils.isBlank(text)) {
      return null;
    }

    return URLEncoder.encode(text, StandardCharsets.UTF_8);
  }

  @Override
  public String decode(String text) {
    if (StringUtils.isBlank(text)) {
      return null;
    }

    return URLDecoder.decode(text, StandardCharsets.UTF_8).replaceAll("\\s+", "+");
  }

  private Key getKeyFromPassword() {
    try {
      SecretKeyFactory factory =
          SecretKeyFactory.getInstance(EncryptionConstants.DERIVATION_FUNCTION);
      byte[] saltBytes = salt.getBytes(StandardCharsets.UTF_8);
      KeySpec spec =
          new PBEKeySpec(
              password.toCharArray(),
              saltBytes,
              EncryptionConstants.ITERATION_COUNT,
              EncryptionConstants.KEY_LENGTH);

      return new SecretKeySpec(
          factory.generateSecret(spec).getEncoded(), EncryptionConstants.AES_ALGORITHM);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new EncryptionException(e);
    }
  }
}
