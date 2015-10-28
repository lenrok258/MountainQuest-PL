package com.lofatsoftware.mountainquest.pl.generator.page;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.lofatsoftware.mountainquest.pl.generator.page.tiles.PageNumberCellGenerator;
import com.lofatsoftware.mountainquest.pl.generator.page.utils.PhraseUtil;

import java.io.IOException;

public class FirstPageGenerator implements PageGenerator {

    @Override
    public PdfPTable generatePage() throws DocumentException, IOException {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100f);
        table.setWidths(new int[]{1});
        table.setExtendLastRow(true);
        table.addCell(generateAuthorCell());
        return table;
    }

    private PdfPCell generateAuthorCell() throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell(PhraseUtil.phrase("...........................\nImię i nazwisko", 14, Font.BOLD));
        cell.setBorder(0);
        cell.setPaddingBottom(10);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setColspan(4);
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return cell;
    }
}
