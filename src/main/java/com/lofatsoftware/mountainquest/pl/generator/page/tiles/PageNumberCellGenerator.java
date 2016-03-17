package com.lofatsoftware.mountainquest.pl.generator.page.tiles;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPCell;
import com.lofatsoftware.mountainquest.pl.generator.page.DataPageGenerator;
import com.lofatsoftware.mountainquest.pl.generator.page.utils.PhraseUtil;

import java.io.IOException;

public class PageNumberCellGenerator {

    private int pageNumber;

    public PageNumberCellGenerator(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public PdfPCell generateTile() throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell(PhraseUtil.phrase("- " + pageNumber + " -"));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(0);
        cell.setPadding(0);
        cell.setPadding(5);
        cell.setPaddingBottom(9);
        cell.setBackgroundColor(DataPageGenerator.BACKGROUND_COLOR);
        return cell;
    }
}
