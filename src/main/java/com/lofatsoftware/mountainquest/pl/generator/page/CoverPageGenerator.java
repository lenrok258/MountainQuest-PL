package com.lofatsoftware.mountainquest.pl.generator.page;

import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.lofatsoftware.mountainquest.pl.generator.page.utils.ImageUtils;

import java.io.File;

public class CoverPageGenerator implements PageGenerator {

    private static final int COVER_MARGIN = 10;

    private File imageFile;
    private PdfWriter pdfWriter;

    public CoverPageGenerator(File imageFile, PdfWriter pdfWriter) {
        this.imageFile = imageFile;
        this.pdfWriter = pdfWriter;
    }

    @Override
    public PdfPTable generatePage() throws Exception {
        Image image = Image.getInstance(imageFile.toURL());
        float heightToWidthRatio = (210f / 297f);
        Image imageCropped = ImageUtils.cropImageToMeetRatio(pdfWriter, image, heightToWidthRatio);

        PdfPCell cell = new PdfPCell(imageCropped, true);
        cell.setBorder(0);
        cell.setPadding(COVER_MARGIN);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setExtraParagraphSpace(0);
        cell.setRightIndent(0);

        PdfPTable table = new PdfPTable(1);
        ;
        table.setWidthPercentage(100f);
        table.setWidths(new int[]{1});
        table.setExtendLastRow(true);
        table.addCell(cell);

        return table;
    }

}
