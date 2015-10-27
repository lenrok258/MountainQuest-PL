package com.lofatsoftware.mountainquest.pl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.lofatsoftware.mountainquest.pl.data.Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.*;
import java.util.List;

public class Generator {

    private static final String RESULT_FILE = "mountain-quest-pl.pdf";
    private static final int PAGE_MARGIN = 20;
    private static final float GOOGLE_MAPS_FOOTER_HEIGHT = 44f;
    private static final int NUMBER_OF_EMPTY_PAGES = 20;

    private final Document document;
    private final PdfWriter writer;
    private final LinkedHashMap<String, Data> tableOfContents = new LinkedHashMap<>();

    private String currentSection = "";

    public Generator() throws FileNotFoundException, DocumentException {
        document = new Document(PageSize.A4, PAGE_MARGIN, PAGE_MARGIN, PAGE_MARGIN, PAGE_MARGIN);
        writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT_FILE));
        document.open();
    }

    public void generatePdf(List<Data> data) throws IOException, DocumentException, InterruptedException {
        int pageNumber = 1;
        for (; pageNumber <= data.size(); pageNumber++) {
            Data dataItem = data.get(pageNumber - 1);
            generatePage(dataItem, pageNumber);
        }

        generateEmptyPages(pageNumber, NUMBER_OF_EMPTY_PAGES);
        generateTableOfContents();
    }

    public void closeDocument() {
        document.close();
    }

    private void generatePage(Data data, int pageNumber) {
        try {
            System.out.println(MessageFormat.format("Page {0}: {1}", pageNumber, data.title));
            Thread.sleep(Math.abs(new Random(System.currentTimeMillis()).nextLong()) % 1000); // trick GMaps API
            document.add(generateMainTable(data, pageNumber));
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
            PdfPTable emptyWithPageNumber = generateEmptyPage(pageNumber);
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
        PdfPCell cell = new PdfPCell(phrase("Spis treści", 18, Font.BOLD));
        cell.setBorder(0);
        cell.setPaddingBottom(10);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setColspan(4);
        return cell;
    }

    private void generateSectionTitle(PdfPTable table, Data data) throws IOException, DocumentException {
        if ( !this.currentSection.equals(data.mountains) ) {
            currentSection = data.mountains;

            PdfPCell cell = new PdfPCell(phrase(currentSection, 14, Font.BOLD));
            cell.setBorder(0);
            cell.setPaddingTop(10);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setColspan(4);
            table.addCell(cell);
        }
    }

    private PdfPCell generateTableOfContentsValue(String value) throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell(phrase(value, 12, Font.BOLD));
        cell.setBorder(0);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPaddingBottom(3);
        cell.setPaddingLeft(10);
        cell.setColspan(3);
        return cell;
    }

    private PdfPCell generateTableOfContentsKey(String key) throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell(phrase(key, 12, Font.BOLD));
        cell.setBorder(0);
        cell.setBorderWidthBottom(1);
        cell.setBorderColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setPaddingBottom(3);
        return cell;
    }

    private PdfPTable generateEmptyPage(int pageNumber) throws DocumentException, IOException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100f);
        table.setWidths(new int[]{1, 1});
        table.setExtendLastRow(true);
        table.addCell(generatePageNumber(pageNumber));
        return table;
    }

    private PdfPTable generateMainTable(Data data, int pageNumber) throws DocumentException, IOException {
        PdfPTable table = new PdfPTable(12); // Bootstrap style
        table.setWidthPercentage(100f);
        table.setWidths(new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        table.setExtendLastRow(true);

        table.addCell(generateHeaderTitle(data));
        table.addCell(generateHeaderData(data));
        table.addCell(generatePhoto(data));
        table.addCell(generateMapImage(data));
        table.addCell(generateDescription(data));
        table.addCell(generateStamp(data));
        table.addCell(generatePageNumber(pageNumber));

        return table;
    }

    private PdfPCell generateHeaderTitle(Data data) throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell(phrase(data.title, 15, Font.BOLD));
        cell.setBorder(0);
        cell.setBorderWidthBottom(1);
        cell.setPaddingTop(10);
        cell.setPaddingBottom(10);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setColspan(7);
        return cell;
    }

    private PdfPCell generateHeaderData(Data data) throws IOException, DocumentException {
        String dataString = MessageFormat.format("{0}\n{1}m n.p.m.\n{2}, {3}",
                data.mountains,
                data.height,
                data.latitude,
                data.longitude);
        PdfPCell cell = new PdfPCell(phrase(dataString, 12));
        cell.setBorder(0);
        cell.setBorderWidthBottom(1);
        cell.setPaddingTop(10);
        cell.setPaddingBottom(10);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(5);
        return cell;
    }

    private PdfPCell generatePhoto(Data data) throws IOException, DocumentException {
        PdfPCell cell;
        if (data.photoUrl == null) {
            cell = new PdfPCell(phrase("No image"));
        } else {
            cell = new PdfPCell(imagePhoto(data.photoUrl), true);
        }
        cell.setBorder(0);
        cell.setPaddingTop(10);
        cell.setPaddingBottom(10);
        cell.setPaddingRight(5);
        cell.setBorderWidthBottom(1);
        cell.setColspan(6);
        return cell;
    }

    private PdfPCell generateMapImage(Data data) throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell(imageMap(data.mapUrl), true);
        cell.setBorder(0);
        cell.setPaddingTop(10);
        cell.setPaddingBottom(10);
        cell.setPaddingLeft(5);
        cell.setBorderWidthBottom(1);
        cell.setColspan(6);
        return cell;
    }

    private PdfPCell generateDescription(Data data) throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell(phrase(data.description, 14));
        cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        cell.setBorder(0);
        cell.setPaddingTop(10);
        cell.setPaddingBottom(15);
        cell.setBorderWidthBottom(1);
        cell.setColspan(12);
        return cell;

    }

    private PdfPCell generatePageNumber(int pageNumber) throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell(phrase("- " + pageNumber + " -"));
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorder(0);
        cell.setPaddingBottom(10);
        cell.setBorderWidthBottom(1);
        cell.setColspan(6);
        return cell;
    }

    private PdfPCell generateStamp(Data data) throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell(phrase("Data i pieczątka"));
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(0);
        cell.setPaddingBottom(10);
        cell.setBorderWidthBottom(1);
        cell.setColspan(6);
        return cell;
    }

    private Phrase phrase(String textToPrint) throws IOException, DocumentException {
        return phrase(textToPrint, 10);
    }

    private Phrase phrase(String textToPrint, float fontSize) throws IOException, DocumentException {
        return phrase(textToPrint, fontSize, Font.NORMAL);
    }

    private Phrase phrase(String textToPrint, float fontSize, int fontStyle) throws IOException, DocumentException {
        return new Phrase(textToPrint, font(fontSize, fontStyle));
    }

    private Font font(float fontSize, int style) throws IOException, DocumentException {
        return FontFactory.getFont("Times-Roman", BaseFont.CP1250, BaseFont.EMBEDDED, fontSize, style);
    }

    private Image imagePhoto(String imagePath) throws DocumentException, IOException {
        URL url = new File(imagePath).toURL();
        Image image = Image.getInstance(url);
        return cropPhotoImage(image);
    }

    private Image imageMap(String mapUrl) throws DocumentException, IOException {
        URL url = new URL(mapUrl);
        Image image = Image.getInstance(url);
        return cropMapImage(image);
    }

    private Image cropMapImage(Image image) throws DocumentException {
        float width = image.getScaledWidth();
        float height = image.getScaledHeight();
        float footerReductionPercentage = GOOGLE_MAPS_FOOTER_HEIGHT / height;
        int widthReduction = (int) (width * footerReductionPercentage);

        return cropImage(image, new CropRectangle(0, widthReduction, 0, GOOGLE_MAPS_FOOTER_HEIGHT));
    }

    private Image cropPhotoImage(Image image) throws DocumentException {
        float desiredProportion = 600f / 800f;
        float imageWidth = image.getScaledWidth();
        float imageHeight = image.getScaledHeight();
        float imageDesiredWidth = imageHeight / desiredProportion;
        float widthToCut = imageWidth - imageDesiredWidth; //TODO: Sprawdzic czy liczba nie jest ujemna. Jeżeli tak to przycinać wysokość zamiast szerokości

        return cropImage(image, new CropRectangle(widthToCut / 2, widthToCut / 2, 0, 0));
    }

    private Image cropImage(Image image, CropRectangle cropRectangle) throws DocumentException {
        float width = image.getScaledWidth();
        float height = image.getScaledHeight();
        PdfTemplate template = writer.getDirectContent().createTemplate(
                width - cropRectangle.leftReduction - cropRectangle.rightReduction,
                height - cropRectangle.topReduction - cropRectangle.bottomReduction);
        template.addImage(image,
                width, 0, 0,
                height, -cropRectangle.leftReduction, -cropRectangle.bottomReduction);
        return Image.getInstance(template);
    }

}

