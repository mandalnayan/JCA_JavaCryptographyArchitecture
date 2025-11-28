package com.iispl.jca.digitalsignature.pdfsign;

	import com.itextpdf.kernel.pdf.*;
	import com.itextpdf.layout.*;
	import com.itextpdf.layout.element.*;

	public class PDFGeneration {
	    public static void main(String[] args) throws Exception {
	        PdfWriter writer = new PdfWriter("output.pdf");
	        PdfDocument pdf = new PdfDocument(writer);
	        Document document = new Document(pdf);

	        document.add(new Paragraph("Hello PDF from Java!\n How are you?"));

	        document.close();
	    }
	}
