package com.iispl.jca.digitalsignature.pdfsign;

import java.io.FileOutputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import javax.security.auth.x500.X500Principal;
import sun.security.x509.*;

public class KeyStoreGenerator {

    public static void generateKeyStore(String keystorePath, String alias, String password) throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();

        X500Principal dnName = new X500Principal("CN=Test User, O=My Organization, C=US");
        X509Certificate certificate = generateSelfSignedCertificate(dnName, keyPair);

        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(null, password.toCharArray());
        keyStore.setKeyEntry(alias, keyPair.getPrivate(), password.toCharArray(), new Certificate[]{certificate});

        try (FileOutputStream fos = new FileOutputStream(keystorePath)) {
            keyStore.store(fos, password.toCharArray());
        }

        System.out.println("KeyStore generated successfully!");
    }

    private static X509Certificate generateSelfSignedCertificate(X500Principal dnName, KeyPair keyPair) throws Exception {
        long validity = 3650L * 24 * 60 * 60 * 1000; // 10 years validity
        Date startDate = new Date();
        Date expiryDate = new Date(startDate.getTime() + validity);

        X509CertInfo certInfo = new X509CertInfo();
        certInfo.set(X509CertInfo.VERSION, new CertificateVersion(CertificateVersion.V3));
        certInfo.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(new SecureRandom().nextInt()));
        certInfo.set(X509CertInfo.SUBJECT, new CertificateSubjectName(dnName));
        certInfo.set(X509CertInfo.ISSUER, new CertificateIssuerName(dnName));
        certInfo.set(X509CertInfo.VALIDITY, new CertificateValidity(startDate, expiryDate));
        certInfo.set(X509CertInfo.KEY, new CertificateX509Key(keyPair.getPublic()));
        certInfo.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(AlgorithmId.get("SHA256withRSA")));

        X509CertImpl certificate = new X509CertImpl(certInfo);
        certificate.sign(keyPair.getPrivate(), "SHA256withRSA");

        return certificate;
    }

    public static void main(String[] args) throws Exception {
        generateKeyStore("keystore.p12", "mykey", "password123");
    }
}
