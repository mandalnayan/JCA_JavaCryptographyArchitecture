package com.iispl.jca.tls;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.security.*;

public class TLSServerWithSSLEngine {
    public static void main(String[] args) throws Exception {
        String keyStorePath = "serverkeystore.jks";
        String keyStorePassword = "password";

        KeyStore keyStore = KeyStore.getInstance("JKS");
        FileInputStream fis = new FileInputStream(keyStorePath);
        keyStore.load(fis, keyStorePassword.toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(keyStore, keyStorePassword.toCharArray());

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), null, null);

        SSLEngine sslEngine = sslContext.createSSLEngine();
        sslEngine.setUseClientMode(false);

        SSLSession session = sslEngine.getSession();
        ByteBuffer appIn = ByteBuffer.allocate(session.getApplicationBufferSize());
        ByteBuffer appOut = ByteBuffer.allocate(session.getApplicationBufferSize());
        ByteBuffer netIn = ByteBuffer.allocate(session.getPacketBufferSize());
        ByteBuffer netOut = ByteBuffer.allocate(session.getPacketBufferSize());

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress("192.168.5.155", 3300));
        System.out.println("TLS Server with SSLEngine is running...");

        SocketChannel socketChannel = serverSocketChannel.accept();
        System.out.println("Client connected: " + socketChannel.getRemoteAddress());

        sslEngine.beginHandshake();
        SSLEngineResult.HandshakeStatus hsStatus = sslEngine.getHandshakeStatus();

        while (hsStatus != SSLEngineResult.HandshakeStatus.FINISHED &&
                hsStatus != SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) {
            switch (hsStatus) {
                case NEED_UNWRAP -> {
                    if (socketChannel.read(netIn) < 0) throw new Exception("Connection closed during handshake");
                    netIn.flip();
                    SSLEngineResult res = sslEngine.unwrap(netIn, appIn);
                    netIn.compact();
                    hsStatus = res.getHandshakeStatus();
                }
                case NEED_WRAP -> {
                    netOut.clear();
                    SSLEngineResult res = sslEngine.wrap(appOut, netOut);
                    hsStatus = res.getHandshakeStatus();
                    netOut.flip();
                    while (netOut.hasRemaining()) {
                        socketChannel.write(netOut);
                    }
                }
                case NEED_TASK -> {
                    Runnable task;
                    while ((task = sslEngine.getDelegatedTask()) != null) {
                        task.run();
                    }
                    hsStatus = sslEngine.getHandshakeStatus();
                }
            }
        }

        System.out.println("TLS handshake completed!");

        while (true) {
            netIn.clear();
            int bytesRead = socketChannel.read(netIn);
            if (bytesRead < 0) break;
            netIn.flip();
            SSLEngineResult res = sslEngine.unwrap(netIn, appIn);
            netIn.compact();
            appIn.flip();
            System.out.println("Client: " + new String(appIn.array(), 0, appIn.limit()));
            appIn.clear();

            String reply = "Hello from TLS Server\n";
            appOut.clear();
            appOut.put(reply.getBytes());
            appOut.flip();
            netOut.clear();
            sslEngine.wrap(appOut, netOut);
            netOut.flip();
            while (netOut.hasRemaining()) {
                socketChannel.write(netOut);
            }
        }

        sslEngine.closeOutbound();
        socketChannel.close();
        serverSocketChannel.close();
    }
}