package com.iispl.jca.tls;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.security.KeyStore;

public class TLSClientWithSSLEngine {
    public static void main(String[] args) throws Exception {
        String trustStorePath = "clienttruststore.jks";
        String trustStorePassword = "password";

        KeyStore trustStore = KeyStore.getInstance("JKS");
        FileInputStream fis = new FileInputStream(trustStorePath);
        trustStore.load(fis, trustStorePassword.toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(trustStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);

        SSLEngine sslEngine = sslContext.createSSLEngine("192.168.5.155", 3300);
        sslEngine.setUseClientMode(true);
        SSLSession session = sslEngine.getSession();

        ByteBuffer appIn = ByteBuffer.allocate(session.getApplicationBufferSize());
        ByteBuffer appOut = ByteBuffer.allocate(session.getApplicationBufferSize());
        ByteBuffer netIn = ByteBuffer.allocate(session.getPacketBufferSize());
        ByteBuffer netOut = ByteBuffer.allocate(session.getPacketBufferSize());

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("192.168.5.155", 3300));

        sslEngine.beginHandshake();
        SSLEngineResult.HandshakeStatus hsStatus = sslEngine.getHandshakeStatus();

        while (hsStatus != SSLEngineResult.HandshakeStatus.FINISHED &&
                hsStatus != SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) {
            switch (hsStatus) {
                case NEED_WRAP -> {
                    netOut.clear();
                    SSLEngineResult res = sslEngine.wrap(appOut, netOut);
                    hsStatus = res.getHandshakeStatus();
                    netOut.flip();
                    while (netOut.hasRemaining()) {
                        socketChannel.write(netOut);
                    }
                }
                case NEED_UNWRAP -> {
                    netIn.clear();
                    int bytesRead = socketChannel.read(netIn);
                    if (bytesRead < 0) throw new Exception("Connection closed during handshake");
                    netIn.flip();
                    SSLEngineResult res = sslEngine.unwrap(netIn, appIn);
                    hsStatus = res.getHandshakeStatus();
                    netIn.compact();
                }
                case NEED_TASK -> {
                    Runnable task;
                    while ((task = sslEngine.getDelegatedTask()) != null) task.run();
                    hsStatus = sslEngine.getHandshakeStatus();
                }
            }
        }

        System.out.println("Handshake complete. Sending message...");

        String msg = "Hello from TLS Client\n";
        appOut.clear();
        appOut.put(msg.getBytes());
        appOut.flip();
        netOut.clear();
        sslEngine.wrap(appOut, netOut);
        netOut.flip();
        while (netOut.hasRemaining()) {
            socketChannel.write(netOut);
        }

        netIn.clear();
        socketChannel.read(netIn);
        netIn.flip();
        sslEngine.unwrap(netIn, appIn);
        appIn.flip();
        System.out.println("Server: " + new String(appIn.array(), 0, appIn.limit()));

        sslEngine.closeOutbound();
        socketChannel.close();
    }
}