package com.lofatsoftware.mountainquest.pl;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;
import com.lofatsoftware.mountainquest.pl.data.Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.List;

public class Generator {

    private static final String RESULT_FILE = "mountain-quest-pl.pdf";
    private static final int PAGE_MARGIN = 20;
    private static final float GOOGLE_MAPS_FOOTER_HEIGHT = 44f;

    private final Document document;
    private final PdfWriter writer;

    public Generator() throws FileNotFoundException, DocumentException {
        document = new Document(PageSize.A4, PAGE_MARGIN, PAGE_MARGIN, PAGE_MARGIN, PAGE_MARGIN);
        writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT_FILE));
        document.open();
    }

    public void generatePdf(List<Data> data) throws IOException, DocumentException {

        for (int i = 0; i < data.size(); i++) {
            Data dataItem = data.get(i);
            System.out.println(dataItem.title);
            generatePage(dataItem, i + 1);
        }
    }

    public void generatePage(Data data, int pageNumber) {
        try {
            document.add(generateMainTable(data, pageNumber));
            document.newPage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeDocument() {
        document.close();
    }

    private PdfPTable generateMainTable(Data data, int pageNumber) throws DocumentException, IOException {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100f);
        table.setWidths(new int[]{1, 1, 1, 1});
        table.setExtendLastRow(true);

        table.addCell(generateTitle(data));
        table.addCell(generateData(data));
        table.addCell(generatePhoto(data));
        table.addCell(generateMapImage(data));
        table.addCell(generateDescription(data));
        table.addCell(generateStamp(data));
        table.addCell(generatePageNumber(pageNumber));

        return table;
    }

    private PdfPCell generateTitle(Data data) throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell(phrase(data.title, 15, Font.BOLD));
        cell.setBorder(0);
        cell.setBorderWidthBottom(1);
        cell.setPaddingTop(10);
        cell.setPaddingBottom(10);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setColspan(3);
        return cell;
    }

    private PdfPCell generateData(Data data) throws IOException, DocumentException {
        String dataString = MessageFormat.format("{0}\n{1}m n.p.m.\n{2}, {3}",
                data.mountains,
                data.height,
                data.latitude,
                data.longitude);
        PdfPCell cell = new PdfPCell(phrase(dataString, 10));
        cell.setBorder(0);
        cell.setBorderWidthBottom(1);
        cell.setPaddingTop(10);
        cell.setPaddingBottom(10);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
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
        cell.setColspan(2);
        return cell;
    }

    private PdfPCell generateMapImage(Data data) throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell(imageMap(data.mapUrl), true);
        cell.setBorder(0);
        cell.setPaddingTop(10);
        cell.setPaddingBottom(10);
        cell.setPaddingLeft(5);
        cell.setBorderWidthBottom(1);
        cell.setColspan(2);
        return cell;
    }

    private PdfPCell generateDescription(Data data) throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell(phrase(data.description, 12));
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        cell.setBorder(0);
        cell.setPaddingTop(10);
        cell.setPaddingBottom(15);
        cell.setBorderWidthBottom(1);
        return cell;

    }

    private PdfPCell generatePageNumber(int pageNumber) throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell(phrase(String.valueOf(pageNumber)));
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setColspan(2);
        cell.setBorder(0);
        cell.setPaddingBottom(10);
        cell.setBorderWidthBottom(1);
        return cell;
    }

    private PdfPCell generateStamp(Data data) throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell(phrase("Data i pieczątka"));
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setColspan(2);
        cell.setBorder(0);
        cell.setPaddingBottom(10);
        cell.setBorderWidthBottom(1);
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
        BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
        return new Font(baseFont, fontSize, style);
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

