package com.iispl.jca.bounclecastle;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.Security;

public class BouncleCastle_Ex {
	 public static void main(String[] args) {
	        Security.addProvider(new BouncyCastleProvider());

	        System.out.println("Providers:");
	        for (var p : Security.getProviders()) {
	            System.out.println(p.getName());
	        }
	    }
}
