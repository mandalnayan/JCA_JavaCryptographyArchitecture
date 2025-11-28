package com.iispl.jca.digitalsignature;

import java.security.*;

/**
 * 4. EdDSA (Ed25519, Ed448) — Newest (Java 15+) and Best ✔ Advantages
 * 
 * Fastest modern signature scheme
 * 
 * Strong security, even against bad randomness
 * 
 * Very safe default parameters
 * 
 * Small key sizes
 * 
 * Very easy to use—no special padding or tricky choices
 * 
 * ❌ Disadvantages
 * 
 * Newer, not supported everywhere (older libraries, some HSMs)
 * 
 * Not always available by default in older JVMs (but supported in modern ones)
 ** 
 *        Best use cases **
 * 
 * APIs and microservices
 * 
 * Cryptographic tokens
 * 
 * Blockchain
 * 
 * Secure messaging (Signal protocol)
 * 
 * Any new system (recommended)
 */

public class DigitalSignature_EDDSA {
	public static void main(String[] args) throws Exception {

		// 1. Generate Ed25519 keypair
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("Ed25519");
		KeyPair keyPair = keyGen.generateKeyPair();

		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();

		String message = "Hello EdDSA (Ed25519)!";

		// 2. Sign
		Signature signer = Signature.getInstance("Ed25519");
		signer.initSign(privateKey);
		signer.update(message.getBytes("UTF-8"));
		byte[] signature = signer.sign();
		System.out.println("Ed25519 Signature: " + bytesToHex(signature));

		// 3. Verify
		Signature verifier = Signature.getInstance("Ed25519");
		verifier.initVerify(publicKey);
		verifier.update(message.getBytes("UTF-8"));

		boolean isValid = verifier.verify(signature);
		System.out.println("Signature valid: " + isValid);
	}

	private static String bytesToHex(byte[] bytes) {
		StringBuilder hex = new StringBuilder();
		for (byte b : bytes) {
			hex.append(String.format("%02x", b));
		}
		return hex.toString();
	}
}
