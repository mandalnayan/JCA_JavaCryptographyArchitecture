package com.iispl.jca.bounclecastle;

import java.security.MessageDigest;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class MessageDigest_BouncleCastle {
	  public static void main(String[] args) throws Exception {
	        Security.addProvider(new BouncyCastleProvider());

	        MessageDigest digest = MessageDigest.getInstance("SHA-256", "BC");

	        byte[] hash = digest.digest("Hello World".getBytes());

	        System.out.println("SHA-256 hash:");
	        for (byte b : hash) {
	            System.out.printf("%02x", b);
	        }
	    }
}
