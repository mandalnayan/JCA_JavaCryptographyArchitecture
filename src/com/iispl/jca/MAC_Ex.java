package com.iispl.jca;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class MAC_Ex {

	private static String toHashString(byte[] hmac) {
		StringBuffer hexString = new StringBuffer();
		
		for (int i = 0; i < hmac.length; i++) {
			hexString.append(Integer.toHexString(0xFF & hmac[i]));
		}
		return hexString.toString();
	}
	
	public static String getMac(String message, String passcode) throws NoSuchAlgorithmException, InvalidKeyException {
		// convert the secret key to a Secretkeyspec
		SecretKeySpec secretKeySpec = new SecretKeySpec(passcode.getBytes(), "HmacSHA256");
		
//		Create an HMAC instace with the SHA-256 algorithm
		Mac hmacSha256 = Mac.getInstance("HmacSHA256");
		
//		Initialize the HMAC with  the secret key
		hmacSha256.init(secretKeySpec);
		
//		Compute the HMAC of the decoded message
		byte[] hmac = hmacSha256.doFinal(message.getBytes());
		
		String str = toHashString(hmac);
		System.out.println(str);
		
		return str;
	}
	
	public static void verifyMessage(String msg, String macHash, String secretKey) throws InvalidKeyException, NoSuchAlgorithmException {
		SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
		
//		Create an HMAC instace with the SHA-256 algorithm
		Mac hmacSha256 = Mac.getInstance("HmacSHA256");
		
//		Initialize the HMAC with  the secret key
		hmacSha256.init(secretKeySpec);
		
//		Compute the HMAC of the decoded message
		byte[] calculatedHmac = hmacSha256.doFinal(msg.getBytes());
		
		String str = toHashString(calculatedHmac);
		
		if (str.equals(macHash)) {
//			If the HMACs match the message is authentic
			System.out.println("Message is valid..!");
		} else {
			System.out.println("Message is inValid..!");
		}
	}
	
	public static void main(String args[]) {
		String orginalMessage = "Hello, this is secret message";
		String passcode = "YourSecretKey";
		
			String mac;
			try {
				mac = getMac(orginalMessage, passcode);
				verifyMessage(orginalMessage+"q", mac, passcode);
			} catch (InvalidKeyException | NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
	
}
