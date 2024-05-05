package com.main;
import java.security.SecureRandom;

public class Utils {
    public static String generateRandomKey(int numBytes) {
        SecureRandom random = new SecureRandom();
        byte[] values = new byte[numBytes];
        random.nextBytes(values);
        StringBuilder sb = new StringBuilder();
        for (byte b : values) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(generateRandomKey(16));  // 16 bytes = 128 bits
    }
}
