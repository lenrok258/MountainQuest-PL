package com.lofatsoftware.mountainquest.pl.generator.page;

import static com.lofatsoftware.mountainquest.pl.generator.page.utils.PhraseUtil.phrase;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.lofatsoftware.mountainquest.pl.data.Data;
import com.lofatsoftware.mountainquest.pl.generator.page.utils.CropRectangle;
import com.lofatsoftware.mountainquest.pl.generator.page.tiles.PageNumberCellGenerator;
import com.lofatsoftware.mountainquest.pl.generator.page.utils.ImageUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;

public class DataPageGenerator implements PageGenerator {

    public static final float GOOGLE_MAPS_FOOTER_HEIGHT = 44f;

    private Data data;
    private int pageNumber;
    private BaseColor backgroundColor;
    private PdfWriter pdfWriter;

    public DataPageGenerator(Data data, int pageNumber, BaseColor backgroundColor, PdfWriter pdfWriter) {
        this.data = data;
        this.pageNumber = pageNumber;
        this.backgroundColor = backgroundColor;
        this.pdfWriter = pdfWriter;
    }

    @Override
    public PdfPTable generatePage() throws DocumentException, IOException {
        PdfPTable table = new PdfPTable(12); // Bootstrap style
        table.setWidthPercentage(100f);
        table.setWidths(new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        table.setExtendLastRow(true);

        table.addCell(generateHeaderTitle());
        table.addCell(generateHeaderData());
        table.addCell(generatePhoto());
        table.addCell(generateMapImage());
        table.addCell(generateDescription());
        table.addCell(generateFooter());

        return table;
    }

    private PdfPCell generateFooter() throws IOException, DocumentException {

        PdfPCell cell = new PdfPCell();
        cell.setColspan(12);
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(0);
        cell.setPadding(0);

        PdfPTable innerTable = new PdfPTable(2);
        innerTable.setWidths(new int[]{1, 1});
        innerTable.setWidthPercentage(100);
        innerTable.setPaddingTop(0);
        innerTable.addCell(generateStamp());
        innerTable.addCell(new PageNumberCellGenerator(pageNumber, backgroundColor).generateTile());

        cell.addElement(innerTable);
        return cell;
    }

    private PdfPCell generateHeaderTitle() throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell(phrase(data.title, 15, Font.BOLD));
        cell.setBorder(0);
        cell.setPaddingTop(10);
        cell.setPaddingBottom(10);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setColspan(7);
        cell.setBackgroundColor(backgroundColor);
        cell.setPaddingLeft(10);
        return cell;
    }

    private PdfPCell generateHeaderData() throws IOException, DocumentException {
        String dataString = MessageFormat.format("{0}\n{1}m n.p.m., {2}km\n{3}, {4}",
                data.mountains,
                data.height,
                data.distanceFromCracow,
                data.latitude,
                data.longitude);
        PdfPCell cell = new PdfPCell(phrase(dataString, 12));
        cell.setBorder(0);
        cell.setPaddingTop(10);
        cell.setPaddingBottom(10);
        cell.setPaddingRight(10);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(5);
        cell.setBackgroundColor(backgroundColor);
        return cell;
    }

    private PdfPCell generatePhoto() throws IOException, DocumentException {
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
        cell.setColspan(6);
        return cell;
    }

    private PdfPCell generateMapImage() throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell(imageMap(data.mapUrl), true);
        cell.setBorder(0);
        cell.setPaddingTop(10);
        cell.setPaddingBottom(10);
        cell.setPaddingLeft(5);
        cell.setColspan(6);
        return cell;
    }

    private PdfPCell generateDescription() throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell(phrase(data.description, 14));
        cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        cell.setBorder(0);
        cell.setPaddingTop(10);
        cell.setPaddingBottom(15);
        cell.setBackgroundColor(backgroundColor);
        cell.setPaddingRight(10);
        cell.setPaddingLeft(10);
        cell.setColspan(12);
        return cell;
    }

    private PdfPCell generateStamp() throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell(phrase("Data i pieczątka"));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(0);
        cell.setPadding(5);
        cell.setPaddingBottom(9);
        cell.setBackgroundColor(backgroundColor);
        return cell;
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

    private Image cropPhotoImage(Image image) throws DocumentException {
        float desiredRatio = 600f / 800f;
        return ImageUtils.cropImageToMeetRatio(pdfWriter, image, desiredRatio);
    }

    private Image cropMapImage(Image image) throws DocumentException {
        float width = image.getScaledWidth();
        float height = image.getScaledHeight();
        float footerReductionPercentage = GOOGLE_MAPS_FOOTER_HEIGHT / height;
        int widthReduction = (int) (width * footerReductionPercentage);
        CropRectangle cropRectangle = new CropRectangle(0, widthReduction, 0, GOOGLE_MAPS_FOOTER_HEIGHT);

        return ImageUtils.cropImageToRectangle(pdfWriter, image, cropRectangle);
    }
}
