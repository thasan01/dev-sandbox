package com.codingronin.sandbox.crypto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class PasswordBasedFileEncrypterTests {

  static final String OUT_DIR = "src/test/resources/";
  static final String PLAIN_TEXT_FILENAME = OUT_DIR + "input-paintext-file.txt";
  static final String ACTUAL_PLAIN_TEXT_FILENAME = OUT_DIR + "actual-paintext-file.txt";
  static final String ACTUAL_PLAIN_TEXT_IV_FILENAME = OUT_DIR + "actual-paintext-iv-file.txt";
  static final String ACTUAL_ENCRYPTED_FILENAME = OUT_DIR + "actual-encrypted-file.txt";
  static final String ACTUAL_ENCRYPTED_IV_FILENAME = OUT_DIR + "actual-encrypted-iv-file.txt";
  static final String EXPECTED_ENCRYPTED_FILENAME = OUT_DIR + "expected-encrypted-file.txt";
  static final String EXPECTED_ENCRYPTED_IV_FILENAME = OUT_DIR + "expected-encrypted-iv-file.txt";

  PasswordBasedFileEncrypter encrypter;


  String salt = "12345678";
  String pass = "abc123";
  byte[] iv = new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};

  @Before
  public void init() throws NoSuchAlgorithmException, NoSuchPaddingException {

    SecureRandom secureRandom = mock(SecureRandom.class, withSettings().withoutAnnotations());

    doAnswer(new Answer<Void>() {
      @Override
      public Void answer(InvocationOnMock invocation) throws Throwable {

        final byte[] values = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        Object[] arguments = invocation.getArguments();
        byte[] bytes = (byte[]) arguments[0];
        for (int i = 0; i < 16; i++) {
          bytes[i] = values[i];
        }

        return null;
      }
    }).when(secureRandom).nextBytes(any(byte[].class));

    Cipher cipher = Cipher.getInstance(PasswordBasedFileEncrypter.CIPHER_ALGORITHM);
    encrypter = new PasswordBasedFileEncrypter(cipher, secureRandom);
  }

  @AfterClass
  public static void destroy() {
    // Clean up all files that were created as part of tests
    File root = new File(OUT_DIR);
    Pattern p = Pattern.compile("actual-.*\\.txt");
    List<File> files =
        Arrays.asList(root.listFiles((File file) -> p.matcher(file.getName()).matches()));
    for (File file : files) {
      file.delete();
    }
  }

  /**
   * 
   * @throws NoSuchAlgorithmException
   * @throws InvalidKeySpecException
   * @throws InvalidKeyException
   * @throws InvalidAlgorithmParameterException
   * @throws BadPaddingException
   * @throws IllegalBlockSizeException
   */
  @Test
  public void encryptStringTest()
      throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException,
      InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException {

    String inputData = "This is a super secret content that needs to be encrypted.";
    SecretKey key = PasswordBasedFileEncrypter.getKeyFromPassword(pass, salt);

    String actual = encrypter.encrypt(inputData, key, iv);
    String expected =
        "1K4Ah7TiK88QKflw631re76U2+aOq4kLp8w+9o2G1KdLKm3I1RI/PEm/5iICUM1lEj3TOe49SMvtyHM7kWh41w==";
    assertEquals(actual, expected);
  }

  /**
   * 
   * @throws NoSuchAlgorithmException
   * @throws InvalidKeySpecException
   * @throws InvalidKeyException
   * @throws InvalidAlgorithmParameterException
   * @throws BadPaddingException
   * @throws IllegalBlockSizeException
   */
  @Test
  public void decryptStringTest()
      throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException,
      InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException {
    String inputData =
        "1K4Ah7TiK88QKflw631re76U2+aOq4kLp8w+9o2G1KdLKm3I1RI/PEm/5iICUM1lEj3TOe49SMvtyHM7kWh41w==";
    SecretKey key = PasswordBasedFileEncrypter.getKeyFromPassword(pass, salt);

    String expected = "This is a super secret content that needs to be encrypted.";
    String actual = encrypter.decrypt(inputData, key, iv);
    assertEquals(actual, expected);
  }

  @Test
  public void encryptFileTest()
      throws InvalidKeyException, InvalidAlgorithmParameterException, BadPaddingException,
      IllegalBlockSizeException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {

    File inputFile = new File(PLAIN_TEXT_FILENAME);
    File outputFile = new File(ACTUAL_ENCRYPTED_FILENAME);
    SecretKey key = PasswordBasedFileEncrypter.getKeyFromPassword(pass, salt);
    encrypter.encryptFile(key, iv, inputFile, outputFile, false);

    byte[] actualBytes = Files.readAllBytes(Paths.get(ACTUAL_ENCRYPTED_FILENAME));
    byte[] expectedBytes = Files.readAllBytes(Paths.get(EXPECTED_ENCRYPTED_FILENAME));
    Arrays.deepEquals(ArrayUtils.toObject(actualBytes), ArrayUtils.toObject(expectedBytes));
    assertTrue(Files.exists(Paths.get(ACTUAL_ENCRYPTED_FILENAME)));
  }

  @Test
  public void encryptFileWithIvTest() throws NoSuchAlgorithmException, InvalidKeySpecException,
      InvalidKeyException, InvalidAlgorithmParameterException, BadPaddingException,
      IllegalBlockSizeException, IOException {
    File inputFile = new File(PLAIN_TEXT_FILENAME);
    File outputFile = new File(ACTUAL_ENCRYPTED_IV_FILENAME);

    SecretKey key = PasswordBasedFileEncrypter.getKeyFromPassword(pass, salt);
    encrypter.encryptFile(key, iv, inputFile, outputFile, true);

    byte[] actualBytes = Files.readAllBytes(Paths.get(ACTUAL_ENCRYPTED_IV_FILENAME));
    byte[] expectedBytes = Files.readAllBytes(Paths.get(EXPECTED_ENCRYPTED_IV_FILENAME));
    Arrays.deepEquals(ArrayUtils.toObject(actualBytes), ArrayUtils.toObject(expectedBytes));
    assertTrue(Files.exists(Paths.get(ACTUAL_ENCRYPTED_IV_FILENAME)));

  }


  @Test
  public void decryptFileTest() throws NoSuchAlgorithmException, InvalidKeySpecException,
      InvalidKeyException, InvalidAlgorithmParameterException, BadPaddingException,
      IllegalBlockSizeException, IOException {

    File inputFile = new File(EXPECTED_ENCRYPTED_FILENAME);
    File outputFile = new File(ACTUAL_PLAIN_TEXT_FILENAME);
    SecretKey key = PasswordBasedFileEncrypter.getKeyFromPassword(pass, salt);
    encrypter.decryptFile(key, iv, inputFile, outputFile);

    byte[] actualBytes = Files.readAllBytes(Paths.get(EXPECTED_ENCRYPTED_FILENAME));
    byte[] expectedBytes = Files.readAllBytes(Paths.get(ACTUAL_PLAIN_TEXT_FILENAME));
    Arrays.deepEquals(ArrayUtils.toObject(actualBytes), ArrayUtils.toObject(expectedBytes));
    assertTrue(Files.exists(Paths.get(ACTUAL_PLAIN_TEXT_FILENAME)));
  }

  @Test
  public void decryptFileIvTest() throws NoSuchAlgorithmException, InvalidKeySpecException,
      InvalidKeyException, InvalidAlgorithmParameterException, BadPaddingException,
      IllegalBlockSizeException, IOException {
    File inputFile = new File(EXPECTED_ENCRYPTED_IV_FILENAME);
    File outputFile = new File(ACTUAL_PLAIN_TEXT_IV_FILENAME);
    SecretKey key = PasswordBasedFileEncrypter.getKeyFromPassword(pass, salt);
    encrypter.decryptFileWithIV(key, inputFile, outputFile);

    byte[] actual = Files.readAllBytes(Paths.get(ACTUAL_PLAIN_TEXT_IV_FILENAME));
    byte[] expected = Files.readAllBytes(Paths.get(PLAIN_TEXT_FILENAME));
    assertTrue(Arrays.deepEquals(ArrayUtils.toObject(actual), ArrayUtils.toObject(expected)));
  }

  @Test
  public void generateIvTest() {
    byte[] expected = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    byte[] actual = encrypter.generateIv();
    assertTrue(Arrays.deepEquals(ArrayUtils.toObject(expected), ArrayUtils.toObject(actual)));
  }

  @Test
  public void generateKeyTest() throws NoSuchAlgorithmException {
    SecretKey key = PasswordBasedFileEncrypter.generateKey(256);
    assertNotNull(key);
  }

}
