package com.lofatsoftware.mountainquest.pl.generator;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.lofatsoftware.mountainquest.pl.generator.page.utils.PhraseUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class CoverGenerator {

    private static final String RESULT_FILE = "mountain-quest_covers.pdf";
    private static final int PAGE_MARGIN = 20;

    private final Document document;
    private final PdfWriter pdfWriter;
    private final List<File> coverFiles;

    public CoverGenerator(List<File> coverFiles) throws FileNotFoundException, DocumentException {
        this.coverFiles = coverFiles;
        this.document = new Document(PageSize.A4.rotate(), PAGE_MARGIN, PAGE_MARGIN, PAGE_MARGIN, PAGE_MARGIN);
        this.pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(RESULT_FILE));
        this.document.open();
    }

    public void generatePdf() throws IOException, DocumentException {

        for (File coverFile : coverFiles) {
            document.add(generateCoverPage(coverFile));
            document.newPage();
        }

        document.close();
    }

    private PdfPTable generateCoverPage(File coverFile) throws IOException, DocumentException {

        Image image = Image.getInstance(coverFile.toURL());

        PdfPCell cell = new PdfPCell(image);
        cell.setBorder(0);

        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100f);
        table.setWidths(new int[]{1});
        table.setExtendLastRow(true);
        table.addCell(cell);

        return table;
    }

}
