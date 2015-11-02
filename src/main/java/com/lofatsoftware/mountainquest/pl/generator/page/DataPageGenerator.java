package com.lofatsoftware.mountainquest.pl.generator.page;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.lofatsoftware.mountainquest.pl.data.Data;
import com.lofatsoftware.mountainquest.pl.generator.page.utils.CropRectangle;
import com.lofatsoftware.mountainquest.pl.generator.page.tiles.PageNumberCellGenerator;
import com.lofatsoftware.mountainquest.pl.generator.page.utils.ImageUtils;
import com.lofatsoftware.mountainquest.pl.generator.page.utils.PhraseUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;

public class DataPageGenerator implements PageGenerator {

    private static final float GOOGLE_MAPS_FOOTER_HEIGHT = 44f;

    private Data data;
    private int pageNumber;
    private PdfWriter pdfWriter;

    public DataPageGenerator(Data data, int pageNumber, PdfWriter pdfWriter) {
        this.data = data;
        this.pageNumber = pageNumber;
        this.pdfWriter = pdfWriter;
    }

    @Override
    public PdfPTable generatePage() throws DocumentException, IOException {
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
        table.addCell(new PageNumberCellGenerator(pageNumber).generateTile());

        return table;
    }

    private PdfPCell generateHeaderTitle(Data data) throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell(PhraseUtil.phrase(data.title, 15, Font.BOLD));
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
        PdfPCell cell = new PdfPCell(PhraseUtil.phrase(dataString, 12));
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
            cell = new PdfPCell(PhraseUtil.phrase("No image"));
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
        PdfPCell cell = new PdfPCell(PhraseUtil.phrase(data.description, 14));
        cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        cell.setBorder(0);
        cell.setPaddingTop(10);
        cell.setPaddingBottom(15);
        cell.setBorderWidthBottom(1);
        cell.setColspan(12);
        return cell;
    }

    private PdfPCell generateStamp(Data data) throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell(PhraseUtil.phrase("Data i pieczątka"));
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(0);
        cell.setPaddingBottom(10);
        cell.setBorderWidthBottom(1);
        cell.setColspan(6);
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
