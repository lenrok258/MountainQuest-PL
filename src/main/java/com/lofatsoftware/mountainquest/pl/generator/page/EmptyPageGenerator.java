package com.lofatsoftware.mountainquest.pl.generator.page;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.lofatsoftware.mountainquest.pl.generator.page.tiles.PageNumberCellGenerator;

import java.io.IOException;

public class EmptyPageGenerator implements PageGenerator {

    private int pageNumber;

    public EmptyPageGenerator(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    @Override
    public PdfPTable generatePage() throws DocumentException, IOException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100f);
        table.setWidths(new int[]{1, 1});
        table.setExtendLastRow(true);
        table.addCell(new PageNumberCellGenerator(pageNumber).generateTile());
        return table;
    }
}
