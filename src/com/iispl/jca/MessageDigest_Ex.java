package com.iispl.jca;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class MessageDigest_Ex {

	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter message: ");
		String message = sc.nextLine();
		
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(message.getBytes());
			byte[] digest = messageDigest.digest();			
			
			toHashString(digest);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void toHashString(byte [] digest) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < digest.length; i++) {
//			convert byte to hexa-decimal
			sb.append(Integer.toHexString(0xFF & digest[i]));			
		}
		System.out.println(sb.toString());
	}
}
