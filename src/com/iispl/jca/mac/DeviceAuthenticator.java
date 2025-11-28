package com.iispl.jca.mac;
import javax.crypto.Mac;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public class DeviceAuthenticator {

    // Algorithm can be "HmacSHA256", "HmacSHA1", etc. "HmacSHA256" is recommended.
    private static final String HMAC_ALGORITHM = "HmacSHA256";

    /**
     * Generates a MAC for the given data using the shared secret key.
     * This is the "sender" side (IoT device sending data).
     */
    public static byte[] generateMac(String data, SecretKey key) 
            throws NoSuchAlgorithmException, InvalidKeyException {
        // Get an instance of the Mac algorithm
        Mac mac = Mac.getInstance(HMAC_ALGORITHM);
        // Initialize the Mac instance with the secret key
        mac.init(key);
        // Compute the MAC of the data
        byte[] macBytes = mac.doFinal(data.getBytes());
        return macBytes;
    }

    /**
     * Verifies the received MAC against the computed MAC of the data.
     * This is the "receiver" side (server verifying data from IoT device).
     */
    public static boolean verifyMac(String data, byte[] receivedMac, SecretKey key) 
            throws NoSuchAlgorithmException, InvalidKeyException {
        // Compute the MAC again on the receiver's side using the same data and key
        byte[] computedMac = generateMac(data, key);
        // Compare the two MACs
        return Arrays.equals(receivedMac, computedMac);
    }

    public static void main(String[] args) {
        try {
            // 1. Establish a shared secret key (pre-shared or via secure key exchange)
        	String passcode = "iispl1221";  // Replace with your actual secret key
        	SecretKey secretKey = new SecretKeySpec(passcode.getBytes(), "HmacSHA256");
            
            String message = "Sensor data: temp 22C, humidity 45%";

            // 2. Sender (IoT Device) generates the MAC
            byte[] generatedMac = generateMac(message, secretKey);
            String macBase64 = Base64.getEncoder().encodeToString(generatedMac);
            System.out.println("Original Message: " + message);
            System.out.println("Generated MAC (Base64): " + macBase64);

            // 3. Receiver (Server) verifies the message integrity and authenticity
            // The message and the MAC are sent over the network
            
            // Example of a valid verification
            boolean isValid = verifyMac(message, generatedMac, secretKey);
            System.out.println("\nVerification Result (Valid): " + isValid);

            // Example of a tampered message (simulated)
            String tamperedMessage = "Sensor data: temp 99C, humidity 45%";
            boolean isTampered = verifyMac(tamperedMessage, generatedMac, secretKey);
            System.out.println("Verification Result (Tampered): " + isTampered);

        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }
}
