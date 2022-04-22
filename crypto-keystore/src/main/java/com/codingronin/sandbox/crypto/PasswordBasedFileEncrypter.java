package com.codingronin.sandbox.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES takes 3 inputs:
 * 
 * Input Data: The data that needs to be encrypted. Can be a String, or a File.
 * 
 * Secret Key: The Symmetric Key used to encrypt and decrypt the file. Can be generated randomly, or
 * from a file.
 * 
 * Initialization Vector (IV)
 * 
 *
 */
public class PasswordBasedFileEncrypter {

  public static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
  static final String ENCRYPTION_TYPE = "AES";
  static final String SECRET_FACTORY_TYPE = "PBKDF2WithHmacSHA256";
  static final int ITERATION_COUNT = 65536;
  static final int KEY_LENGTH = 256;

  Cipher cipher;
  SecureRandom secureRandom;

  public PasswordBasedFileEncrypter(Cipher cipher, SecureRandom secureRandom) {
    this.cipher = cipher;
    this.secureRandom = secureRandom;
  }

  /**
   * 
   * @param input
   * @param key
   * @param iv
   * @return
   * @throws InvalidAlgorithmParameterException
   * @throws InvalidKeyException
   * @throws BadPaddingException
   * @throws IllegalBlockSizeException
   */
  public String encrypt(String input, SecretKey key, byte[] iv)
      throws InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException,
      IllegalBlockSizeException {

    cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
    byte[] cipherText = cipher.doFinal(input.getBytes());
    return Base64.getEncoder().encodeToString(cipherText);
  }

  /**
   * 
   * @param cipherText
   * @param key
   * @param iv
   * @return
   * @throws InvalidAlgorithmParameterException
   * @throws InvalidKeyException
   * @throws BadPaddingException
   * @throws IllegalBlockSizeException
   */
  public String decrypt(String cipherText, SecretKey key, byte[] iv)
      throws InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException,
      IllegalBlockSizeException {

    cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
    byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
    return new String(plainText);
  }


  public void encryptFile(SecretKey key, byte[] iv, File inputFile, File outputFile,
      boolean includeIVInFile) throws IOException, InvalidAlgorithmParameterException,
      InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

    cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
    try (FileInputStream inputStream = new FileInputStream(inputFile);
        FileOutputStream outputStream = new FileOutputStream(outputFile)) {
      byte[] buffer = new byte[64];
      int bytesRead;

      if (includeIVInFile) {
        int ivSize = iv.length;
        outputStream.write(ivSize);
        outputStream.write(iv);
      }

      while ((bytesRead = inputStream.read(buffer)) != -1) {
        byte[] output = cipher.update(buffer, 0, bytesRead);
        if (output != null) {
          outputStream.write(output);
        }
      }

      byte[] outputBytes = cipher.doFinal();
      if (outputBytes != null) {
        outputStream.write(outputBytes);
      }
    }

  }


  /**
   * 
   * @param key
   * @param iv
   * @param encryptedFile
   * @param decryptedFile
   * @throws IOException
   * @throws InvalidAlgorithmParameterException
   * @throws InvalidKeyException
   * @throws BadPaddingException
   * @throws IllegalBlockSizeException
   */
  public void decryptFile(SecretKey key, byte[] iv, File encryptedFile, File decryptedFile)
      throws IOException, InvalidAlgorithmParameterException, InvalidKeyException,
      BadPaddingException, IllegalBlockSizeException {
    cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));

    try (FileInputStream inputStream = new FileInputStream(encryptedFile);
        FileOutputStream outputStream = new FileOutputStream(decryptedFile);) {

      byte[] buffer = new byte[64];
      int bytesRead;
      while ((bytesRead = inputStream.read(buffer)) != -1) {
        byte[] output = cipher.update(buffer, 0, bytesRead);
        if (output != null) {
          outputStream.write(output);
        }
      }
      byte[] output = cipher.doFinal();
      if (output != null) {
        outputStream.write(output);
      }
    }
  }

  /**
   * 
   * @param key
   * @param encryptedFile
   * @param decryptedFile
   * @throws IOException
   * @throws InvalidAlgorithmParameterException
   * @throws InvalidKeyException
   * @throws BadPaddingException
   * @throws IllegalBlockSizeException
   */
  public void decryptFileWithIV(SecretKey key, File encryptedFile, File decryptedFile)
      throws IOException, InvalidAlgorithmParameterException, InvalidKeyException,
      BadPaddingException, IllegalBlockSizeException {

    try (FileInputStream inputStream = new FileInputStream(encryptedFile);
        FileOutputStream outputStream = new FileOutputStream(decryptedFile);) {

      int ivlen = inputStream.read();
      byte[] ivBytes = new byte[ivlen];
      inputStream.read(ivBytes, 0, ivlen);

      IvParameterSpec iv = new IvParameterSpec(ivBytes);
      cipher.init(Cipher.DECRYPT_MODE, key, iv);

      byte[] buffer = new byte[64];
      int bytesRead;

      while ((bytesRead = inputStream.read(buffer)) != -1) {
        byte[] output = cipher.update(buffer, 0, bytesRead);
        if (output != null) {
          outputStream.write(output);
        }
      }

      byte[] output = cipher.doFinal();
      if (output != null) {
        outputStream.write(output);
      }
    }
  }


  /**
   * 
   * @return
   */
  public byte[] generateIv() {
    byte[] iv = new byte[16];
    secureRandom.nextBytes(iv);
    return iv;
  }

  /**
   * Randomly generates SecretKey of given size.
   * 
   * @param keySize acceptable values: 28, 192, and 256
   * @return
   * @throws NoSuchAlgorithmException
   */
  public static SecretKey generateKey(int keySize) throws NoSuchAlgorithmException {
    KeyGenerator keyGenerator = KeyGenerator.getInstance(ENCRYPTION_TYPE);
    keyGenerator.init(keySize);
    return keyGenerator.generateKey();
  }

  /**
   * 
   * @param password
   * @param salt
   * @return
   * @throws NoSuchAlgorithmException
   * @throws InvalidKeySpecException
   */
  public static SecretKey getKeyFromPassword(String password, String salt)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    char[] passArray = password.toCharArray();
    SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_FACTORY_TYPE);
    KeySpec spec = new PBEKeySpec(passArray, salt.getBytes(), ITERATION_COUNT, KEY_LENGTH);
    return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), ENCRYPTION_TYPE);
  }

}
