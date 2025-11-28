package com.iispl.jca.digitalsignature;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.ECGenParameterSpec;


/**
 * ✔ Advantages

Much smaller keys (256-bit EC ≈ 3072-bit RSA)

Very fast (especially verifying)

Strong security with less computation

Good for mobile, embedded, IoT

❌ Disadvantages

More complex implementation

If randomness is weak → signatures break (nonce reuse vulnerability)

Patent history (not a problem today, but historically affected adoption)

** Best use cases **

Modern web authentication

JWT tokens

Blockchain (Bitcoin, Ethereum)

High-performance systems
 * 
 */



public class DigitalSignature_ECDSA {

	private static String KEY_GENERATOR_ALGO = "EC";
	private static String DIGITAL_SIGNATURE_ALGO = "SHA256withECDSA";

	public static void main(String args[]) {
		try {
			String message = "Hello";
			KeyPair keyPair = generateKeyPair();

			byte[] bytes = addDigitalSignature(message, keyPair.getPrivate());
			boolean isVerified = verifyDigitalSignature(message, bytes, keyPair.getPublic());
			if (isVerified) {
				System.out.println("Message is verified..!");
			} else {
				System.out.println("Message is not verified..!");
			}

		} catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException | InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static KeyPair generateKeyPair() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
		  // 1. Generate EC KeyPair
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(KEY_GENERATOR_ALGO);
        keyGen.initialize(new ECGenParameterSpec("secp256r1")); // also known as P-256
        KeyPair keyPair = keyGen.generateKeyPair();
		return keyPair;
	}

	public static byte[] addDigitalSignature(String message, PrivateKey privateKey)
			throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {

//		Creating a Signature object
		Signature sign = Signature.getInstance(DIGITAL_SIGNATURE_ALGO);

//		Initialize the signature
		sign.initSign(privateKey);

		byte[] bytes = message.getBytes();
		System.out.println(new String(bytes, StandardCharsets.UTF_8));

//		Adding data to the signature
		sign.update(bytes);

//		Calculating the signature
		byte[] signed = sign.sign();

		return signed;

	}

	public static boolean verifyDigitalSignature(String message, byte[] signed, PublicKey publicKey)
			throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
//		Creating a Signature object
		Signature sign = Signature.getInstance(DIGITAL_SIGNATURE_ALGO);
		sign.initVerify(publicKey);
		sign.update(message.getBytes());
		return sign.verify(signed);
	}

}
