package com.iispl.jca.digitalsignature.pdfsign;

	import java.io.FileInputStream;
	import java.io.FileOutputStream;
	import java.security.KeyStore;
	import java.security.PrivateKey;
	import java.security.Security;
	import java.security.cert.Certificate;

	import org.bouncycastle.jce.provider.BouncyCastleProvider;

	import com.itextpdf.kernel.pdf.PdfReader;
	import com.itextpdf.kernel.pdf.StampingProperties;
	import com.itextpdf.signatures.BouncyCastleDigest;
	import com.itextpdf.signatures.IExternalDigest;
	import com.itextpdf.signatures.IExternalSignature;
	import com.itextpdf.signatures.PdfSignatureAppearance;
	import com.itextpdf.signatures.PdfSigner;
	import com.itextpdf.signatures.PrivateKeySignature;

	public class PdfSignerUtil {

	    public static void signPdf(String src, String dest, PrivateKey privateKey, Certificate[] chain) throws Exception {
	        // Create PdfSigner (Corrected Constructor)
	        PdfSigner signer = new PdfSigner(new PdfReader(src), new FileOutputStream(dest), new StampingProperties());

	        // Set signature appearance
	        PdfSignatureAppearance appearance = signer.getSignatureAppearance()
	                .setReason("Document Approval")
	                .setLocation("India")
	                .setReuseAppearance(false);
	        signer.setFieldName("Signature1");

	        // Create signature container
	        IExternalSignature pks = new PrivateKeySignature(privateKey, "SHA256", "BC");
	        IExternalDigest digest = new BouncyCastleDigest();

	        // Sign the document
	        signer.signDetached(digest, pks, chain, null, null, null, 0, PdfSigner.CryptoStandard.CMS);
	        
	        System.out.println("PDF signed successfully: " + dest);
	    }

	    public static void main(String[] args) throws Exception {
	    	Security.addProvider(new BouncyCastleProvider());
	    	 String srcPdf = "output.pdf"; // Input unsigned PDF
		        String destPdf = "signed_document.pdf"; // Output signed PDF
		        String keystorePath = "nayan.keystore"; // Keystore file
		        String keystorePassword = "123456"; // Keystore password

		        // Load keystore
		        KeyStore ks = KeyStore.getInstance("PKCS12");
		        ks.load(new FileInputStream(keystorePath), keystorePassword.toCharArray());

		        // Get private key and certificate chain
		        String alias = ks.aliases().nextElement();
		        PrivateKey privateKey = (PrivateKey) ks.getKey(alias, keystorePassword.toCharArray());
		        Certificate[] chain = ks.getCertificateChain(alias);

		        // Sign PDF
		        signPdf(srcPdf, destPdf, privateKey, chain);
		    	System.out.println("hii");
	    }
	}