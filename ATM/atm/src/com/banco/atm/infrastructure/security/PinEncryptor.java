package com.banco.atm.infrastructure.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PinEncryptor {

    // Cambiado de Base64 a SHA-256 — Base64 NO es cifrado, solo codificación
    public static String encrypt(String pin) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(pin.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al cifrar NIP", e);
        }
    }
}
