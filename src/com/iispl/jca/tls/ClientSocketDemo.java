package com.iispl.jca.tls;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManagerFactory;

public class ClientSocketDemo {
	public static void main(String[] args) {

			System.out.println("Client is connecting to server...");
			// Create a new socket channel
			try (SocketChannel socketChannel = SocketChannel.open()) {
				// Connect to the server at localhost on port 8080
				boolean isConnected = socketChannel.connect(new InetSocketAddress("localhost", 8080));
				System.out.println("Is server available:" + isConnected);
				startCommunication(socketChannel);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	static void startCommunication(SocketChannel selectedSocket) {
		try {
			if (selectedSocket != null && selectedSocket.isConnected()) {
				ByteBuffer buffer = ByteBuffer.allocate(256);
				String message;
				do {
					int bytesRead = selectedSocket.read(buffer);
					// Print the server's response
					if (bytesRead != -1) {
						message = new String(buffer.array(), 0, bytesRead);
						System.out.println("Received: " + message);
					}
					System.out.println("Enter your msg: (bye to quit)");
					message = new java.util.Scanner(System.in).nextLine();
					buffer.flip();
					selectedSocket.write(buffer);
					buffer.clear();
				} while (!message.equals("bye"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
