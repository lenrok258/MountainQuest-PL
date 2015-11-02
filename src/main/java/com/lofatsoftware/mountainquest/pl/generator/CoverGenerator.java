package com.lofatsoftware.mountainquest.pl.generator;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class CoverGenerator {

    private static final String RESULT_FILE = "mountain-quest_covers.pdf";
    private static final int PAGE_MARGIN = 20;

    private final Document document;
    private final PdfWriter pdfWriter;

    public CoverGenerator() throws FileNotFoundException, DocumentException {
        document = new Document(PageSize.A4, PAGE_MARGIN, PAGE_MARGIN, PAGE_MARGIN, PAGE_MARGIN);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(RESULT_FILE));
        document.open();
    }

    public void generatePdf() {
        //document.close();
    }

}
