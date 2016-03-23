package com.lofatsoftware.mountainquest.pl.generator.page.tiles;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPCell;
import com.lofatsoftware.mountainquest.pl.generator.page.DataPageGenerator;
import com.lofatsoftware.mountainquest.pl.generator.page.utils.PhraseUtil;

import java.io.IOException;

public class PageNumberCellGenerator {

    private int pageNumber;
    private BaseColor backgroundColor;

    public PageNumberCellGenerator(int pageNumber, BaseColor backgroundColor) {
        this.pageNumber = pageNumber;
        this.backgroundColor = backgroundColor;
    }

    public PdfPCell generateTile() throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell(PhraseUtil.phrase("- " + pageNumber + " -"));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(0);
        cell.setPadding(5);
        cell.setPaddingRight(10);
        cell.setPaddingBottom(9);
        cell.setBackgroundColor(backgroundColor);
        return cell;
    }
}
