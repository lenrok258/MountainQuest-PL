package com.lofatsoftware.mountainquest.pl.generator;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.lofatsoftware.mountainquest.pl.data.Data;
import com.lofatsoftware.mountainquest.pl.generator.page.DataPageGenerator;
import com.lofatsoftware.mountainquest.pl.generator.page.EmptyPageGenerator;
import com.lofatsoftware.mountainquest.pl.generator.page.utils.PhraseGenerator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;
import java.util.List;

public class Generator {

    private static final String RESULT_FILE = "mountain-quest-pl.pdf";
    private static final int PAGE_MARGIN = 20;

    private static final int NUMBER_OF_EMPTY_PAGES = 20;

    private final Document document;
    private final PdfWriter pdfWriter;
    private final LinkedHashMap<String, Data> tableOfContents = new LinkedHashMap<>();

    private String currentSection = "";

    public Generator() throws FileNotFoundException, DocumentException {
        document = new Document(PageSize.A4, PAGE_MARGIN, PAGE_MARGIN, PAGE_MARGIN, PAGE_MARGIN);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(RESULT_FILE));
        document.open();
    }

    public void generatePdf(List<Data> data) throws IOException, DocumentException, InterruptedException {

        int pageNumber = 1;
        for (; pageNumber <= data.size(); pageNumber++) {
            Data dataItem = data.get(pageNumber - 1);
            generateDataPage(dataItem, pageNumber);
        }

        generateEmptyPages(pageNumber, NUMBER_OF_EMPTY_PAGES);
        generateTableOfContents();
    }

    public void closeDocument() {
        document.close();
    }

    private void generateDataPage(Data data, int pageNumber) {
        try {
            System.out.println(MessageFormat.format("Page {0}: {1}", pageNumber, data.title));
            PdfPTable dataTable = new DataPageGenerator(data, pageNumber, pdfWriter).generatePage();
            document.add(dataTable);
            document.newPage();
            tableOfContents.put(String.valueOf(pageNumber), data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateEmptyPages(final int previousPageNumber, final int howManyPages) throws DocumentException, IOException {
        for (int i = 0; i < howManyPages; i++) {
            int pageNumber = previousPageNumber + i;
            System.out.println(MessageFormat.format("Page {0}: Empty page", pageNumber));
            PdfPTable emptyWithPageNumber = new EmptyPageGenerator(pageNumber).generatePage();
            document.add(emptyWithPageNumber);
            document.newPage();
        }
    }

    private void generateTableOfContents() throws DocumentException, IOException {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100f);
        table.setWidths(new int[]{1, 1, 1, 1});

        table.addCell(generateTableOfContentsTitle());

        for (String key : tableOfContents.keySet()) {
            generateSectionTitle(table, tableOfContents.get(key));
            table.addCell(generateTableOfContentsValue(tableOfContents.get(key).title));
            table.addCell(generateTableOfContentsKey(key));
        }

        document.add(table);
        document.newPage();
    }

    private PdfPCell generateTableOfContentsTitle() throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell(PhraseGenerator.phrase("Spis treści", 18, Font.BOLD));
        cell.setBorder(0);
        cell.setPaddingBottom(10);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setColspan(4);
        return cell;
    }

    private void generateSectionTitle(PdfPTable table, Data data) throws IOException, DocumentException {
        if ( !this.currentSection.equals(data.mountains) ) {
            currentSection = data.mountains;

            PdfPCell cell = new PdfPCell(PhraseGenerator.phrase(currentSection, 14, Font.BOLD));
            cell.setBorder(0);
            cell.setPaddingTop(10);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setColspan(4);
            table.addCell(cell);
        }
    }

    private PdfPCell generateTableOfContentsValue(String value) throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell(PhraseGenerator.phrase(value, 12, Font.BOLD));
        cell.setBorder(0);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPaddingBottom(3);
        cell.setPaddingLeft(10);
        cell.setColspan(3);
        return cell;
    }

    private PdfPCell generateTableOfContentsKey(String key) throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell(PhraseGenerator.phrase(key, 12, Font.BOLD));
        cell.setBorder(0);
        cell.setBorderWidthBottom(1);
        cell.setBorderColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setPaddingBottom(3);
        return cell;
    }

}

