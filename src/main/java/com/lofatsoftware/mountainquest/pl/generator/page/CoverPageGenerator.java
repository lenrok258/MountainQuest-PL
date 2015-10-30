package com.lofatsoftware.mountainquest.pl.generator.page;

import com.itextpdf.text.pdf.PdfPTable;

import java.io.File;

public class CoverPageGenerator implements PageGenerator {

    private File imageFile;

    public CoverPageGenerator(File imageFile) {
        this.imageFile = imageFile;
    }

    @Override
    public PdfPTable generatePage() throws Exception {
        return null;
    }

}
