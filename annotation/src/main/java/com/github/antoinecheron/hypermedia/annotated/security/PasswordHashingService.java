package com.github.antoinecheron.hypermedia.annotated.security;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Source code copied from
 * https://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
 */
public class PasswordHashingService {

  public static String generateStrongPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
    final int iterations = 1000;
    final char[] chars = password.toCharArray();
    final byte[] salt = getSalt();

    final PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
    final SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    final byte[] hash = skf.generateSecret(spec).getEncoded();
    return iterations + ":" + toHex(salt) + ":" + toHex(hash);
  }

  public static boolean validatePassword(String originalPassword, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
    final String[] parts = storedPassword.split(":");
    final int iterations = Integer.parseInt(parts[0]);
    final byte[] salt = fromHex(parts[1]);
    final byte[] hash = fromHex(parts[2]);

    final PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
    final SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    final byte[] testHash = skf.generateSecret(spec).getEncoded();

    int diff = hash.length ^ testHash.length;
    for(int i = 0; i < hash.length && i < testHash.length; i++) {
      diff |= hash[i] ^ testHash[i];
    }
    return diff == 0;
  }

  private static byte[] getSalt() throws NoSuchAlgorithmException {
    final SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
    final byte[] salt = new byte[16];
    sr.nextBytes(salt);
    return salt;
  }

  private static String toHex(byte[] array) {
    final BigInteger bi = new BigInteger(1, array);
    final String hex = bi.toString(16);
    int paddingLength = (array.length * 2) - hex.length();
    if(paddingLength > 0) {
      return String.format("%0"  +paddingLength + "d", 0) + hex;
    } else {
      return hex;
    }
  }

  private static byte[] fromHex(String hex) {
    final byte[] bytes = new byte[hex.length() / 2];
    for(int i = 0; i<bytes.length ;i++) {
      bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
    }
    return bytes;
  }

}
