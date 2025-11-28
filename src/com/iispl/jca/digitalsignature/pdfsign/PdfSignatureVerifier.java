package com.iispl.jca.digitalsignature.pdfsign;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.List;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.signatures.PdfPKCS7;
import com.itextpdf.signatures.SignatureUtil;

public class PdfSignatureVerifier {

    public static boolean verifySignature(String signedPdfPath) throws Exception {
               // Load the signed PDF
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(signedPdfPath));
        SignatureUtil sigUtil = new SignatureUtil(pdfDoc);
        List<String> sigNames = sigUtil.getSignatureNames();

        if (sigNames.isEmpty()) {
            System.out.println("No digital signature found!");
            return false;
        }

        for (String name : sigNames) {
            PdfPKCS7 pkcs7 = sigUtil.readSignatureData(name);
            Certificate[] certs = pkcs7.getCertificates();
            X509Certificate cert = (X509Certificate) certs[0];

            System.out.println("Signature Name: " + name);
            System.out.println("Signer: " + cert.getSubjectDN());

            // 🔹 FIX: Use PdfPKCS7.verifySignatureIntegrityAndAuthenticity()
            boolean isValid = pkcs7.verifySignatureIntegrityAndAuthenticity();
            System.out.println("Signature validity: " + isValid);
            return isValid;
        }

        return false;
    }

    public static void main(String[] args) throws Exception {
        boolean isValid = verifySignature("/home/administrator/Downloads/Acct_Statement_XXXXXXXX1982_04082025.pdf");
        System.out.println("Signature is " + (isValid ? "VALID" : "INVALID"));
    }
}
