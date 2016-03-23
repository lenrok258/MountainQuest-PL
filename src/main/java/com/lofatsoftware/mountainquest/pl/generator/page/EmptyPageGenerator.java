package com.lofatsoftware.mountainquest.pl.generator.page;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.lofatsoftware.mountainquest.pl.generator.page.utils.BackgroundColorGenerator;
import com.lofatsoftware.mountainquest.pl.generator.page.tiles.PageNumberCellGenerator;

import java.io.IOException;

public class EmptyPageGenerator implements PageGenerator {

    private int pageNumber;

    public EmptyPageGenerator() {
    }

    public EmptyPageGenerator(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    @Override
    public PdfPTable generatePage() throws DocumentException, IOException {
        return pageNumber > 0 ? tableWithPageNumber() : emptyTable();
    }

    private PdfPTable tableWithPageNumber() throws DocumentException, IOException {

        PdfPTable innerTable = new PdfPTable(1);
        innerTable.setWidths(new int[]{1});
        innerTable.setWidthPercentage(100);
        innerTable.setPaddingTop(0);
        innerTable.addCell(new PageNumberCellGenerator(pageNumber, BackgroundColorGenerator.DEFAULT_BACKGROUND_COLOR)
                .generateTile());

        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(0);
        cell.setPadding(0);
        cell.addElement(innerTable);

        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100f);
        table.setWidths(new int[]{1});
        table.setExtendLastRow(true);
        table.addCell(cell);
        return table;
    }

    private PdfPTable emptyTable() throws DocumentException {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100f);
        table.setWidths(new int[]{1});
        table.setExtendLastRow(true);
        PdfPCell cell = new PdfPCell(Phrase.getInstance(" "));
        cell.setBorder(0);
        table.addCell(cell);
        return table;
    }
}
