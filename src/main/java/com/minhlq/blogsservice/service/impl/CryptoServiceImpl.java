package com.minhlq.blogsservice.service.impl;

import com.minhlq.blogsservice.constant.CryptoConstants;
import com.minhlq.blogsservice.exception.CryptoException;
import com.minhlq.blogsservice.service.CryptoService;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
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
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
@Service
public class CryptoServiceImpl implements CryptoService {

  @Value("${crypto.secret.password}")
  private String password;

  @Value("${crypto.secret.salt}")
  private String salt;

  private final SecureRandom random = new SecureRandom();

  @Override
  public String encrypt(String text) {
    try {
      if (StringUtils.isBlank(text)) {
        return null;
      }
      byte[] iv = new byte[CryptoConstants.GCM_IV_LENGTH];
      random.nextBytes(iv);

      Cipher cipher = Cipher.getInstance(CryptoConstants.ENCRYPT_ALGORITHM);
      GCMParameterSpec ivSpec =
          new GCMParameterSpec(CryptoConstants.GCM_TAG_LENGTH * Byte.SIZE, iv);
      cipher.init(Cipher.ENCRYPT_MODE, getKeyFromPassword(), ivSpec);

      byte[] ciphertext = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
      byte[] encrypted = new byte[iv.length + ciphertext.length];
      System.arraycopy(iv, 0, encrypted, 0, iv.length);
      System.arraycopy(ciphertext, 0, encrypted, iv.length, ciphertext.length);

      return Base64.getEncoder().encodeToString(encrypted);
    } catch (NoSuchAlgorithmException
        | IllegalArgumentException
        | InvalidKeyException
        | InvalidAlgorithmParameterException
        | IllegalBlockSizeException
        | BadPaddingException
        | NoSuchPaddingException e) {
      log.debug(CryptoConstants.ERROR_ENCRYPTING_DATA, e);
      throw new CryptoException(e);
    }
  }

  @Override
  public String decrypt(String encryptedText) {
    try {
      if (StringUtils.isBlank(encryptedText)) {
        return null;
      }
      byte[] decoded = Base64.getDecoder().decode(encryptedText);
      byte[] iv = Arrays.copyOfRange(decoded, 0, CryptoConstants.GCM_IV_LENGTH);

      Cipher cipher = Cipher.getInstance(CryptoConstants.ENCRYPT_ALGORITHM);
      GCMParameterSpec ivSpec =
          new GCMParameterSpec(CryptoConstants.GCM_TAG_LENGTH * Byte.SIZE, iv);
      cipher.init(Cipher.DECRYPT_MODE, getKeyFromPassword(), ivSpec);

      byte[] ciphertext =
          cipher.doFinal(
              decoded,
              CryptoConstants.GCM_IV_LENGTH,
              decoded.length - CryptoConstants.GCM_IV_LENGTH);

      return new String(ciphertext, StandardCharsets.UTF_8);
    } catch (NoSuchAlgorithmException
        | IllegalArgumentException
        | InvalidKeyException
        | InvalidAlgorithmParameterException
        | IllegalBlockSizeException
        | BadPaddingException
        | NoSuchPaddingException e) {
      log.debug(CryptoConstants.ERROR_DECRYPTING_DATA, e);
      throw new CryptoException(e);
    }
  }

  @Override
  public String encode(String text) {
    try {
      if (StringUtils.isBlank(text)) {
        return null;
      }
      return URLEncoder.encode(text, StandardCharsets.UTF_8.toString());
    } catch (UnsupportedEncodingException e) {
      log.debug(CryptoConstants.ERROR_DECRYPTING_DATA, e);
      throw new CryptoException(e);
    }
  }

  @Override
  public String decode(String text) {
    try {
      if (StringUtils.isBlank(text)) {
        return null;
      }
      return URLDecoder.decode(text, StandardCharsets.UTF_8.toString()).replaceAll("\\s+", "+");
    } catch (UnsupportedEncodingException e) {
      log.debug(CryptoConstants.ERROR_DECRYPTING_DATA, e);
      throw new CryptoException(e);
    }
  }

  private SecretKey getKeyFromPassword() {
    try {
      SecretKeyFactory factory = SecretKeyFactory.getInstance(CryptoConstants.DERIVATION_FUNCTION);
      byte[] saltBytes = salt.getBytes(StandardCharsets.UTF_8);
      KeySpec spec =
          new PBEKeySpec(
              password.toCharArray(),
              saltBytes,
              CryptoConstants.ITERATION_COUNT,
              CryptoConstants.KEY_LENGTH);

      return new SecretKeySpec(
          factory.generateSecret(spec).getEncoded(), CryptoConstants.AES_ALGORITHM);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new CryptoException(e);
    }
  }
}
