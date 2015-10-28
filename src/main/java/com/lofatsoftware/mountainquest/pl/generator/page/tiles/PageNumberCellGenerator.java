package com.lofatsoftware.mountainquest.pl.generator.page.tiles;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPCell;
import com.lofatsoftware.mountainquest.pl.generator.page.utils.PhraseUtil;

import java.io.IOException;

public class PageNumberCellGenerator implements TileGenerator {

    private int pageNumber;

    public PageNumberCellGenerator(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    @Override
    public PdfPCell generateTile() throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell(PhraseUtil.phrase("- " + pageNumber + " -"));
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorder(0);
        cell.setPaddingBottom(10);
        cell.setBorderWidthBottom(1);
        cell.setColspan(6);
        return cell;
    }
}
