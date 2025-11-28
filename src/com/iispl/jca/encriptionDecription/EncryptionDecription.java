package com.iispl.jca.encriptionDecription;

import java.util.Base64;


/*************  You can find complete working experience of Base64 at below      ****************/


/**
 * String encodMess = Base64.getEncoder().encodeToString(message.getBytes());
 * 
 * bytes decode[] = Base64.getDecodder().decode(encodMess.getBytes());
 * String decodeMess = new String(decode);
 */

public class EncryptionDecription {

	public void copyingArray() {
		String arr1[] = {"one", "two", "three"};
		String arr2[] = {"four", "five", "six"};
		String arr3[] = new String[6];
		
//		Copy arr1 element into arr3
		System.arraycopy(arr1, 0, arr3, 0, arr1.length);

//		Copy arr2 element into arr3
		System.arraycopy(arr2, 0, arr3, 3, arr2.length);
		for (String s : arr3) {
			System.out.print(s + " ");
		}
		
		System.out.println();
	}
	
	
	
	
	
	public String encriptMessage(String mess) {
		String encodedMess = Base64.getEncoder().encodeToString(mess.getBytes());
		return encodedMess;
	}
	
	public String decriptMessage(String encodedMess) {
		byte mess[] = Base64.getDecoder().decode(encodedMess.getBytes());
		return new String(mess);
	}
	
	public static void main(String args[]) {
		
		String orginalMess = "H";
		System.out.println("Original Mess: " + orginalMess);
		
		EncryptionDecription obj = new EncryptionDecription();
		obj.copyingArray();
		
		String encriptedMess = obj.encriptMessage(orginalMess);
		System.out.println("Ecnripted: " + encriptedMess);
		String decrpetMess = obj.encriptMessage(encriptedMess);
		System.out.println("Decripted: " + orginalMess);
	}
	
}
