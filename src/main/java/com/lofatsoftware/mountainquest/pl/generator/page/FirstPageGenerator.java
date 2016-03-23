package com.lofatsoftware.mountainquest.pl.generator.page;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.lofatsoftware.mountainquest.pl.generator.page.utils.BackgroundColorGenerator;
import com.lofatsoftware.mountainquest.pl.generator.page.utils.ImageUtils;
import com.lofatsoftware.mountainquest.pl.generator.page.utils.PhraseUtil;

import java.io.IOException;
import java.net.URISyntaxException;

public class FirstPageGenerator implements PageGenerator {

    private PdfWriter pdfWriter;

    public FirstPageGenerator(PdfWriter pdfWriter) {
        this.pdfWriter = pdfWriter;
    }

    @Override
    public PdfPTable generatePage() throws DocumentException, IOException, URISyntaxException {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100f);
        table.setWidths(new int[]{1});
        table.setExtendLastRow(true);
        table.addCell(generateTitleCell());
        table.addCell(generateImageCell());
        table.addCell(generateAuthorCell());
        return table;
    }

    private PdfPCell generateImageCell() throws DocumentException, IOException, URISyntaxException {
        Image image = ImageUtils.readImageFromResources("first-page.jpg");
        Image imageCropped = ImageUtils.cropImageToMeetRatio(pdfWriter, image, 100f / 150f);

        PdfPCell cell = new PdfPCell(imageCropped, true);
        cell.setPaddingTop(10);
        cell.setPaddingBottom(10);
        cell.setBorder(0);
        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    private PdfPCell generateTitleCell() throws IOException, DocumentException {
        //PdfPCell cell = new PdfPCell(PhraseUtil.phrase("Kolekcjoner pieczątek", 25, Font.BOLD));
        PdfPCell cell = new PdfPCell(PhraseUtil.phrase(" ", 25, Font.BOLD));
        cell.setBorder(0);
        cell.setPaddingTop(130);
        cell.setPaddingBottom(20);
        cell.setPaddingLeft(20);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BackgroundColorGenerator.DEFAULT_BACKGROUND_COLOR);
        return cell;
    }

    private PdfPCell generateAuthorCell() throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell(PhraseUtil.phrase(
                "............................................\nImię i nazwisko", 15, Font.NORMAL));
        cell.setBorder(0);
        cell.setPadding(10);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setBackgroundColor(BackgroundColorGenerator.DEFAULT_BACKGROUND_COLOR);
        return cell;
    }
}
