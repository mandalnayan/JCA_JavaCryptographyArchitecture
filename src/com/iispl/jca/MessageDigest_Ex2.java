package com.iispl.jca;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;

public class MessageDigest_Ex2 {

	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter message: ");
		String message = sc.nextLine();
		
		MessageDigest_Ex2 mdObj = new MessageDigest_Ex2();
		
		try {
			String encodedMessage = mdObj.sender(message);
			mdObj.receiverVerify(encodedMessage);
			
			
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String sender(String message) throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		messageDigest.update(message.getBytes());
		byte[] digest = messageDigest.digest();		
		
		toHashString(digest);  // printing digest before encoding
		
		byte[] mess = message.getBytes();
		
		byte wrapper[] = new byte[digest.length + mess.length];
		
		System.arraycopy(digest, 0, wrapper, 0, digest.length);
		System.arraycopy(mess, 0, wrapper, digest.length, mess.length);
		System.out.println("Before encoding combined payload: " + wrapper);
		String encodedString = Base64.getEncoder().encodeToString(wrapper);
		return encodedString;
	}
	
	public void receiverVerify(String encodedDigest) throws NoSuchAlgorithmException {
		byte decoded[] = Base64.getDecoder().decode(encodedDigest.getBytes());
		
		System.out.println("Decoded String: " + decoded);
		
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update("Y".getBytes());
		byte currentMD[] = md.digest();
		System.out.print("Receiver - Current Hash Value: ");
		toHashString(currentMD); 
		
		byte receivedMD[] = new byte[decoded.length - currentMD.length];
		System.arraycopy(decoded, currentMD.length, receivedMD, 0, decoded.length - currentMD.length);
		
		System.out.println(new String(receivedMD));
	}
	
	public static void toHashString(byte [] digest) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < digest.length; i++) {
//			convert byte to hexa-decimal
			sb.append(Integer.toHexString(0xFF & digest[i]));			
		}
		System.out.println(new String(sb));
	}
}
