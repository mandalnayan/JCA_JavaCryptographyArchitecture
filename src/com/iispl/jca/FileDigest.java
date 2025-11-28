package com.iispl.jca;

	import java.net.URL;
	import java.nio.file.Files;
	import java.nio.file.Paths;

	/*
	 * Checking is file is currupted or not
	 */
	public class FileDigest {
	    public static void main(String[] args) {
	        try {
	            String fileURL = "https://example.com/file.zip";
	            String savePath = "file.zip";

	            URL url = new URL(fileURL);
	            Files.copy(url.openStream(), Paths.get(savePath));

	            System.out.println("Download complete!");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}

