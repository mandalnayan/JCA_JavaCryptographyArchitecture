package com.iispl.jca.encriptionDecription;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;


/**
 * Performing encryption with Asymetric key
 */
public class CipherKeyPair_Ex2 {

	private static String ENCRIPTION_ALGROTHIM = "RSA";
	
	private static KeyPair generateSecretKey() throws Exception{
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ENCRIPTION_ALGROTHIM);	
		//keyGen.initialize(2048);
		SecureRandom secRandom = new SecureRandom();
		keyGen.initialize(2048, secRandom);
		System.out.println(secRandom.nextInt());

		KeyPair key = keyGen.generateKeyPair();
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
	private static String encrypt(String data, PublicKey publicKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance(ENCRIPTION_ALGROTHIM);
		cipher.init(cipher.ENCRYPT_MODE, publicKey);
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
	private static String decrypt(String encryptedData, PrivateKey privateKey) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
		Cipher cipher = Cipher.getInstance(ENCRIPTION_ALGROTHIM);
		cipher.init(cipher.DECRYPT_MODE, privateKey);
		byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
		return new String(decryptedBytes);
	}
	
	
	public static void main(String args[]) throws Exception {
		String message = "Hello, how are you?";
		
//		Generated key
		KeyPair keyPair = generateSecretKey();
		
//		Encrypting
		String encryptedData = encrypt(message, keyPair.getPublic());
		
//		Decrypting
		String decyptedMess = decrypt(encryptedData, keyPair.getPrivate());
		
		System.out.println("Orginal message: " + message);
		System.out.println("Decrypted message: " + decyptedMess);
	}
	
	
}
