package com.lofatsoftware.mountainquest.pl.generator.page.utils;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.BaseFont;

import java.io.IOException;

public abstract class PhraseGenerator {

    public static com.itextpdf.text.Phrase phrase(String textToPrint) throws IOException, DocumentException {
        return phrase(textToPrint, 10);
    }

    public static com.itextpdf.text.Phrase phrase(String textToPrint, float fontSize) throws IOException, DocumentException {
        return phrase(textToPrint, fontSize, Font.NORMAL);
    }

    public static com.itextpdf.text.Phrase phrase(String textToPrint, float fontSize, int fontStyle) throws IOException, DocumentException {
        return new com.itextpdf.text.Phrase(textToPrint, font(fontSize, fontStyle));
    }

    public static Font font(float fontSize, int style) throws IOException, DocumentException {
        return FontFactory.getFont("Times-Roman", BaseFont.CP1250, BaseFont.EMBEDDED, fontSize, style);
    }

}
