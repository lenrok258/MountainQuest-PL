package com.lofatsoftware.mountainquest.pl.generator;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.lofatsoftware.mountainquest.pl.data.Data;
import com.lofatsoftware.mountainquest.pl.generator.page.DataPageGenerator;
import com.lofatsoftware.mountainquest.pl.generator.page.EmptyPageGenerator;
import com.lofatsoftware.mountainquest.pl.generator.page.FirstPageGenerator;
import com.lofatsoftware.mountainquest.pl.generator.page.TableOfContentsPageGenerator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;
import java.util.List;

public class ContentGenerator {

    private static final String RESULT_FILE = "mountain-quest-pl.pdf";
    private static final int PAGE_MARGIN = 20;
    private static final int NUMBER_OF_EMPTY_PAGES = 20;

    private final Document document;
    private final PdfWriter pdfWriter;
    private final LinkedHashMap<String, Data> tableOfContents = new LinkedHashMap<>();

    public ContentGenerator() throws FileNotFoundException, DocumentException {
        document = new Document(PageSize.A4, PAGE_MARGIN, PAGE_MARGIN, PAGE_MARGIN, PAGE_MARGIN);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(RESULT_FILE));
        document.open();
    }

    public void generatePdf(List<Data> data) throws IOException, DocumentException, InterruptedException {

        generateFirstPage();

        int pageNumber = 1;
        for (; pageNumber <= data.size(); pageNumber++) {
            Data dataItem = data.get(pageNumber - 1);
            generateDataPage(dataItem, pageNumber);
        }

        generateEmptyPages(pageNumber, NUMBER_OF_EMPTY_PAGES);
        generateTableOfContents();

        document.close();
    }

    private void generateFirstPage() throws IOException, DocumentException {
        PdfPTable firstPage = new FirstPageGenerator().generatePage();
        document.add(firstPage);
        document.newPage();
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
        PdfPTable pdfPTable = new TableOfContentsPageGenerator(tableOfContents).generatePage();
        document.add(pdfPTable);
        document.newPage();
    }
}

