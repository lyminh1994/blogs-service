package com.minhlq.blogsservice.service.impl;

import com.minhlq.blogsservice.exception.EncryptionException;
import com.minhlq.blogsservice.service.EncryptionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

import static com.minhlq.blogsservice.constant.EncryptionConstants.AES_ALGORITHM;
import static com.minhlq.blogsservice.constant.EncryptionConstants.DECRYPTING_DATA_ERROR;
import static com.minhlq.blogsservice.constant.EncryptionConstants.DERIVATION_FUNCTION;
import static com.minhlq.blogsservice.constant.EncryptionConstants.ENCRYPTING_DATA_ERROR;
import static com.minhlq.blogsservice.constant.EncryptionConstants.ENCRYPT_ALGORITHM;
import static com.minhlq.blogsservice.constant.EncryptionConstants.GCM_IV_LENGTH;
import static com.minhlq.blogsservice.constant.EncryptionConstants.GCM_TAG_LENGTH;
import static com.minhlq.blogsservice.constant.EncryptionConstants.ITERATION_COUNT;
import static com.minhlq.blogsservice.constant.EncryptionConstants.KEY_LENGTH;
import static java.nio.charset.StandardCharsets.UTF_8;

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

      byte[] iv = new byte[GCM_IV_LENGTH];
      random.nextBytes(iv);

      Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORITHM);
      GCMParameterSpec ivSpec = new GCMParameterSpec(GCM_TAG_LENGTH * Byte.SIZE, iv);
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
      log.debug(ENCRYPTING_DATA_ERROR, e);
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
      byte[] iv = Arrays.copyOfRange(decoded, 0, GCM_IV_LENGTH);

      Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORITHM);
      GCMParameterSpec ivSpec = new GCMParameterSpec(GCM_TAG_LENGTH * Byte.SIZE, iv);
      cipher.init(Cipher.DECRYPT_MODE, getKeyFromPassword(), ivSpec);

      byte[] ciphertext = cipher.doFinal(decoded, GCM_IV_LENGTH, decoded.length - GCM_IV_LENGTH);

      return new String(ciphertext, UTF_8);
    } catch (NoSuchAlgorithmException
        | IllegalArgumentException
        | InvalidKeyException
        | InvalidAlgorithmParameterException
        | IllegalBlockSizeException
        | BadPaddingException
        | NoSuchPaddingException e) {
      log.debug(DECRYPTING_DATA_ERROR, e);
      throw new EncryptionException(e);
    }
  }

  @Override
  public String encode(String text) {
    if (StringUtils.isBlank(text)) {
      return null;
    }

    return URLEncoder.encode(text, UTF_8);
  }

  @Override
  public String decode(String text) {
    if (StringUtils.isBlank(text)) {
      return null;
    }

    return URLDecoder.decode(text, UTF_8).replaceAll("\\s+", "+");
  }

  private Key getKeyFromPassword() {
    try {
      SecretKeyFactory factory = SecretKeyFactory.getInstance(DERIVATION_FUNCTION);
      byte[] saltBytes = salt.getBytes(UTF_8);
      KeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, ITERATION_COUNT, KEY_LENGTH);

      return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), AES_ALGORITHM);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new EncryptionException(e);
    }
  }
}
