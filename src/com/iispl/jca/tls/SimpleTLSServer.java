package com.iispl.jca.tls;

import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;

public class SimpleTLSServer {
    public static void main(String[] args) {
        String keyStorePath = "serverkeystore.jks";
        String keyStorePassword = "password";

        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            FileInputStream keyStoreIS = new FileInputStream(keyStorePath);
            keyStore.load(keyStoreIS, keyStorePassword.toCharArray());

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(keyStore, keyStorePassword.toCharArray());

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), null, null);

            SSLServerSocketFactory ssf = sslContext.getServerSocketFactory();
            SSLServerSocket sslServerSocket = (SSLServerSocket) ssf.createServerSocket(3300);

            System.out.println("TLS Server is listening on port 3300...");

            SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
            System.out.println("Client connected: " + sslSocket.getInetAddress());

            BufferedReader in = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sslSocket.getOutputStream()));

            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("Received: " + line);
                out.write("Echo: " + line + "\n");
                out.flush();
            }

            sslSocket.close();
            sslServerSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}