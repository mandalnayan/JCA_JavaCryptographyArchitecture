package com.iispl.jca.tls;

import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;

public class SimpleTLSClient {
    public static void main(String[] args) {
        String trustStorePath = "clienttruststore.jks";
        String trustStorePassword = "password";

        try {
            KeyStore trustStore = KeyStore.getInstance("JKS");
            FileInputStream tsFile = new FileInputStream(trustStorePath);
            trustStore.load(tsFile, trustStorePassword.toCharArray());

            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(trustStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

            SSLSocketFactory factory = sslContext.getSocketFactory();
            SSLSocket socket = (SSLSocket) factory.createSocket("192.168.5.155", 3300);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Connected to TLS server.");

            String input;
            while ((input = userInput.readLine()) != null) {
                out.write(input + "\n");
                out.flush();
                String response = in.readLine();
                System.out.println("Server says: " + response);
            }

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}