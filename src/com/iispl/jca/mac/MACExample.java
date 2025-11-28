package com.iispl.jca.mac;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class MACExample {
	
	private static String HMAC_SHA256_ALGORITHM = "HmacSHA256";
	
	private static SecretKey generateSecretKey() throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance(HMAC_SHA256_ALGORITHM);
		keyGenerator.init(256);
		return keyGenerator.generateKey();
	}
	
	//Converting the byte array in to HexString format
		static String toHashString(byte[] hmac) {
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < hmac.length; i++) {
				hexString.append(Integer.toHexString(0xFF & hmac[i]));
			}
			return hexString.toString();
		}
		
    public static String getMac(String message, SecretKey secretKey) throws NoSuchAlgorithmException, InvalidKeyException {
          

            // Create an HMAC instance with the SHA-256 algorithm
            Mac hmacSha256 = Mac.getInstance("HmacSHA256");

            // Initialize the HMAC with the secret key
            hmacSha256.init(secretKey);

            // Compute the HMAC of the message
            byte[] hmac = hmacSha256.doFinal(message.getBytes());
            String str = toHashString(hmac); 
            System.out.println(str);
            return str;
    }

    public static void verifyMessage(String msg, String macHash, SecretKey secretKey) throws NoSuchAlgorithmException, InvalidKeyException {
          
            // Create an HMAC instance with the SHA-256 algorithm
            Mac hmacSha256 = Mac.getInstance("HmacSHA256");

            // Initialize the HMAC with the secret key
            hmacSha256.init(secretKey);

            // Compute the HMAC of the decoded message
            byte[] calculatedHmac = hmacSha256.doFinal(msg.getBytes());
            
            String str = toHashString(calculatedHmac);
            // Compare the computed HMAC with the received HMAC
            if (str.equals(macHash)) {
                // If the HMACs match, the message is authentic
                System.out.println("Valid msg");
            } else {
                // If the HMACs don't match, the message may have been tampered with
                System.out.println("Invalid MAC: Message integrity compromised.");
            }
    }

    public static void main(String[] args) {
        String originalMessage = "Hello, this is a secret message";
		try {
			SecretKey secretKey = generateSecretKey(); // generating the secret key
			String mac = getMac(originalMessage, secretKey);
			verifyMessage(originalMessage, mac, secretKey);
		} catch (InvalidKeyException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
        
    }
}
