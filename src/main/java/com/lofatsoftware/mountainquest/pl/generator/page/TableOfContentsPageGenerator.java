package com.lofatsoftware.mountainquest.pl.generator.page;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.lofatsoftware.mountainquest.pl.data.Data;
import com.lofatsoftware.mountainquest.pl.generator.page.utils.BackgroundColorGenerator;
import com.lofatsoftware.mountainquest.pl.generator.page.utils.PhraseUtil;

import java.io.IOException;
import java.util.LinkedHashMap;

public class TableOfContentsPageGenerator implements PageGenerator {

    private BackgroundColorGenerator backgroundColorGenerator = new BackgroundColorGenerator();
    private LinkedHashMap<String, Data> tableOfContents;
    private String currentSection = "";
    private BaseColor currentBackgroundColor;

    //TODO: For the sake of simplicity it should take data List<Data>
    public TableOfContentsPageGenerator(LinkedHashMap<String, Data> tableOfContents) {
        this.tableOfContents = tableOfContents;
    }

    @Override
    public PdfPTable generatePage() throws DocumentException, IOException {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100f);
        table.setWidths(new int[]{1, 1, 1, 1});

        table.addCell(generateTableOfContentsTitle());

        for (String key : tableOfContents.keySet()) {
            generateSectionTitle(table, tableOfContents.get(key));
            table.addCell(generateTableOfContentsValue(tableOfContents.get(key).title));
            table.addCell(generateTableOfContentsKey(key));
        }
        return table;
    }

    private PdfPCell generateTableOfContentsTitle() throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell(PhraseUtil.phrase("Spis treści", 18, Font.BOLD));
        cell.setBorder(0);
        cell.setPaddingBottom(10);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setColspan(4);
        return cell;
    }

    private void generateSectionTitle(PdfPTable table, Data data) throws IOException, DocumentException {
        if (!this.currentSection.equals(data.mountains)) {
            currentSection = data.mountains;
            currentBackgroundColor = backgroundColorGenerator.generate(data);

            PdfPCell cellSeparator = new PdfPCell(PhraseUtil.phrase(" "));
            cellSeparator.setBorder(0);
            cellSeparator.setPadding(10);
            cellSeparator.setColspan(4);
            table.addCell(cellSeparator);

            PdfPCell cell = new PdfPCell(PhraseUtil.phrase(currentSection, 14, Font.BOLD));
            cell.setBorder(0);
            cell.setPadding(10);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setColspan(4);
            cell.setBackgroundColor(currentBackgroundColor);
            table.addCell(cell);
        }
    }

    private PdfPCell generateTableOfContentsValue(String value) throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell(PhraseUtil.phrase(value, 12, Font.BOLD));
        cell.setBorder(0);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPaddingBottom(3);
        cell.setPaddingLeft(10);
        cell.setColspan(3);
        return cell;
    }

    private PdfPCell generateTableOfContentsKey(String key) throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell(PhraseUtil.phrase(key, 12, Font.BOLD));
        cell.setBorder(0);
        cell.setBorderWidthBottom(1);
        cell.setBorderColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setPaddingBottom(3);
        return cell;
    }
}
