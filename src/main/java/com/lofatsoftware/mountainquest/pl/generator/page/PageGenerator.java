package com.lofatsoftware.mountainquest.pl.generator.page;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;

import java.io.IOException;

interface PageGenerator {

    PdfPTable generatePage() throws Exception;
}
