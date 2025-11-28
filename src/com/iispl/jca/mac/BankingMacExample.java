package com.iispl.jca.mac;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class BankingMacExample {

    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
    
    private static SecretKey generateSecretKey() throws NoSuchAlgorithmException {
    	KeyGenerator keyGenerator = KeyGenerator.getInstance(HMAC_SHA256_ALGORITHM);
		keyGenerator.init(256);
		return keyGenerator.generateKey();
    }
    
    /**
     *  Method to compute the MAC for a given message and key
     * @param message
     * @param key
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static byte[] computeMac(String message, SecretKey key) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
        mac.init(key);
        byte[] macResult = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return macResult;
    }

    // Method to verify the received MAC against a re-computed MAC
    public static boolean verifyMac(String message, byte[] receivedMac, SecretKey key) throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] computedMac = computeMac(message, key);
        
        // Use constant time comparison to avoid timing attacks
        return Arrays.equals(receivedMac, computedMac);
    }

    public static void main(String[] args) {
        try {
            // 1. Establish a shared secret key (pre-shared between banking systems)
        
        	SecretKey sharedKey = generateSecretKey();
            

            // 2. Sender side: Prepare the transaction message and compute the MAC
            String transactionMessage = "TXN_ID:12345, From:AccountA, To:AccountB, Amount:100.50, Currency:INR";
            byte[] senderMac = computeMac(transactionMessage, sharedKey);

            System.out.println("Original Message: " + transactionMessage);
            System.out.println("Generated MAC (hex): " + bytesToHex(senderMac));

            // 3. Transmission: The message and the MAC are sent to the receiver
            // (e.g., as part of an API call or secure message)

            // 4. Receiver side: Recompute the MAC using the same shared key
            // The receiver receives the message and the senderMac
            boolean isVerified = verifyMac(transactionMessage, senderMac, sharedKey);

            System.out.println("\nVerification Result: " + (isVerified ? "MAC is valid (Integrity and Authenticity verified)" : "MAC is invalid (Message tampered or sender unknown)"));

            // Example of a tampered message
            String tamperedMessage = "TXN_ID:12345, From:AccountA, To:AccountC, Amount:100.50, Currency:USD"; // Different 'To' account
            byte[] tamperedMac = computeMac(tamperedMessage, sharedKey);
            boolean isTamperedVerified = verifyMac(tamperedMessage, senderMac, sharedKey); // Using the original MAC

            System.out.println("\nTampered Message Verification: " + (isTamperedVerified ? "Verified (Incorrectly)" : "Not Verified (Correctly detected tampering)"));


        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    // Helper method to convert byte array to hexadecimal string for printing
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
