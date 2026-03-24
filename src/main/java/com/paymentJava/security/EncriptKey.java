package com.paymentJava.security;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class EncriptKey {

  public static String encriptKey(String password) throws Exception {
    byte[] iv = new byte[12];
    new SecureRandom().nextBytes(iv);

    byte[] salt = new byte[16];
    new SecureRandom().nextBytes(salt);

    String secret = "Teste"; // System.getenv("APP_SECRET");
    SecretKeySpec key = deriveKey(secret, salt);

    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
    GCMParameterSpec spec = new GCMParameterSpec(128, iv);

    cipher.init(Cipher.ENCRYPT_MODE, key, spec);
    byte[] cypherText = cipher.doFinal(password.getBytes());

    ByteBuffer buffer = ByteBuffer.allocate(salt.length + iv.length + cypherText.length);
    buffer.put(salt);
    buffer.put(iv);
    buffer.put(cypherText);

    return Base64.getEncoder().encodeToString(buffer.array());

  }

  public static String decriptKey(String data) throws Exception {
    byte[] decoded = Base64.getDecoder().decode(data);
    ByteBuffer buffer = ByteBuffer.wrap(decoded);

    byte[] salt = new byte[16];
    buffer.get(salt);

    byte[] iv = new byte[12];
    buffer.get(iv);

    byte[] cypherText = new byte[buffer.remaining()];
    buffer.get(cypherText);

    String secret = "Teste"; // System.getenv("APP_SECRET");
    SecretKeySpec key = deriveKey(secret, salt);

    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
    GCMParameterSpec spec = new GCMParameterSpec(128, iv);

    cipher.init(Cipher.DECRYPT_MODE, key, spec);

    byte[] original = cipher.doFinal(cypherText);

    return new String(original, StandardCharsets.UTF_8);
  }

  public static SecretKeySpec deriveKey(String password, byte[] salt) throws Exception {
    PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

    byte[] keyBytes = factory.generateSecret(spec).getEncoded();

    return new SecretKeySpec(keyBytes, "AES");
  }
}
