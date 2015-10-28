package com.lofatsoftware.mountainquest.pl.generator.page;

import com.itextpdf.text.pdf.PdfPTable;

public class EmptyPageGenerator implements PageGenerator {

    @Override
    public PdfPTable generatePage() {
        return null;
    }
}
