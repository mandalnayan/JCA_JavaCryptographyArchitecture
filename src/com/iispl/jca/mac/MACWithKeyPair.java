package com.iispl.jca.mac;

	import java.io.FileInputStream;
	import java.io.IOException;
	import java.io.ObjectInputStream;
	import java.security.InvalidKeyException;
	import java.security.NoSuchAlgorithmException;
	import java.util.Arrays;
	import java.util.Base64;
	import java.util.Scanner;

	import javax.crypto.Mac;
	import javax.crypto.spec.SecretKeySpec;

	public class MACWithKeyPair {

		public static String decodeMessage() throws ClassNotFoundException, IOException {
			try {
				FileInputStream fis = new FileInputStream("mactest");
				ObjectInputStream ois = new ObjectInputStream(fis);
				Object o = ois.readObject();
				String data = (String) o;
				System.out.println("Got message " + data);
				System.out.println("Enter the secret key");
				Scanner sc = new Scanner(System.in);
				String secretKey = sc.nextLine();
				
				// Convert the secret key to a SecretKeySpec
				SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
				// Create an HMAC instance with the SHA-256 algorithm
				Mac hmacSha256 = Mac.getInstance("HmacSHA256");
				// Initialize the HMAC with the secret key
				hmacSha256.init(secretKeySpec);
				// Decode the Base64 encoded message
				byte[] combined = Base64.getDecoder().decode(data);
				// Split the combined message and HMAC
				byte[] messageBytes = new byte[combined.length - hmacSha256.getMacLength()];
				byte[] receivedHmac = new byte[hmacSha256.getMacLength()];
				System.arraycopy(combined, 0, messageBytes, 0, messageBytes.length);
				System.arraycopy(combined, messageBytes.length, receivedHmac, 0, receivedHmac.length);
				// Compute the HMAC of the decoded message
				byte[] calculatedHmac = hmacSha256.doFinal(messageBytes);
				// Compare the computed HMAC with the received HMAC
				if (Arrays.equals(calculatedHmac, receivedHmac)) {
					// If the HMACs match, the message is authentic
					return new String(messageBytes);
				} else {
					// If the HMACs don't match, the message may have been tampered with
					return "Invalid MAC: Message integrity compromised.";
				}

			} catch (NoSuchAlgorithmException | InvalidKeyException e) {
				e.printStackTrace();
				return "Error during decoding: " + e.getMessage();
			}
		}

		public static void main(String[] args) {

			String decodedMessage;
			try {
				decodedMessage = decodeMessage();
				System.out.println("Decoded Message: " + decodedMessage);
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
	}
