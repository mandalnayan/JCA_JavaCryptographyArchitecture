package com.iispl.jca.encriptionDecription;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;

public class Cipher_Ex {

	private static String ENCRIPTION_ALGROTHIM = "AES";
	
	private static Key generateSecretKey() throws Exception{
		KeyGenerator keyGen = KeyGenerator.getInstance(ENCRIPTION_ALGROTHIM);		
		SecureRandom secRandom = new SecureRandom();
		keyGen.init(128);
		
		Key key = keyGen.generateKey();
		return key;
	}
	
	/**
	 * Encrypte the message
	 * @param data
	 * @param key
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	private static String encrypt(String data, Key key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance(ENCRIPTION_ALGROTHIM);
		cipher.init(cipher.ENCRYPT_MODE, key);
		byte[] encryptedBytes = cipher.doFinal(data.getBytes());
		return Base64.getEncoder().encodeToString(encryptedBytes);		
	}
	
	/**
	 * Decrypte message
	 * @param encryptedData
	 * @param key
	 * @return
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 */
	private static String decrypt(String encryptedData, Key key) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
		Cipher cipher = Cipher.getInstance(ENCRIPTION_ALGROTHIM);
		cipher.init(cipher.DECRYPT_MODE, key);
		byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
		return new String(decryptedBytes);
	}
	
	public static void main(String args[]) throws Exception {
		String message = "Hello, how are you?";
		
//		Generated key
		Key key = generateSecretKey();
		
//		Encrypting
		String encryptedData = encrypt(message, key);
		
//		Decrypting
		String decyptedMess = decrypt(encryptedData, key);
		
		System.out.println("Orginal message: " + message);
		System.out.println("Decrypted message: " + decyptedMess);
	}
	
	
}
