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
import com.lofatsoftware.mountainquest.pl.generator.page.utils.PhraseUtil;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
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
    public PdfPTable generatePage() throws DocumentException, IOException, URISyntaxException {
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

    private PdfPCell generateHeaderData() throws IOException, DocumentException, URISyntaxException {
        String textMountain = MessageFormat.format("{0}", data.mountains);
        PdfPCell cellMountain = new PdfPCell(PhraseUtil.phrase(textMountain, 12));
        cellMountain.setBorder(0);
        cellMountain.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMountain.setVerticalAlignment(Element.ALIGN_CENTER);
        cellMountain.setPadding(0);
        cellMountain.setPaddingBottom(4);

        PdfPCell cellMountainIcon = new PdfPCell(ImageUtils.readImageFromResources("icons/mountains01.png"), true);
        cellMountainIcon.setBorder(0);
        cellMountainIcon.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMountainIcon.setFixedHeight(10);

        String heightMountain = MessageFormat.format("{0}m n.p.m., {1}km", data.height, data.distanceFromCracow);
        PdfPCell cellHeight = new PdfPCell(PhraseUtil.phrase(heightMountain, 12));
        cellHeight.setBorder(0);
        cellHeight.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellHeight.setVerticalAlignment(Element.ALIGN_CENTER);
        cellHeight.setPadding(0);
        cellHeight.setPaddingBottom(4);

        PdfPCell cellHeightIcon = new PdfPCell(ImageUtils.readImageFromResources("icons/map01.png"), true);
        cellHeightIcon.setBorder(0);
        cellHeightIcon.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellHeightIcon.setFixedHeight(10);

        String coordinatesMountain = MessageFormat.format("{0}, {1}", data.latitude, data.longitude);
        PdfPCell cellCoordinates = new PdfPCell(PhraseUtil.phrase(coordinatesMountain, 12));
        cellCoordinates.setBorder(0);
        cellCoordinates.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellCoordinates.setVerticalAlignment(Element.ALIGN_CENTER);
        cellCoordinates.setPadding(0);
        cellCoordinates.setPaddingBottom(4);

        PdfPCell cellCoordinatesIcon = new PdfPCell(ImageUtils.readImageFromResources("icons/compas.png"), true);
        cellCoordinatesIcon.setBorder(0);
        cellCoordinatesIcon.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellCoordinatesIcon.setFixedHeight(10);

        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidths(new float[] {10, 1});
        headerTable.addCell(cellMountain);
        headerTable.addCell(cellMountainIcon);
        headerTable.addCell(cellHeight);
        headerTable.addCell(cellHeightIcon);
        headerTable.addCell(cellCoordinates);
        headerTable.addCell(cellCoordinatesIcon);

        PdfPCell cell = new PdfPCell(headerTable);
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
        cell.setPaddingLeft(10);
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
