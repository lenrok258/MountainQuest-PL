package com.lofatsoftware.mountainquest.pl.generator.page.utils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;

import java.io.IOException;

public abstract class PhraseUtil {

    public static Phrase phrase(String textToPrint) throws IOException, DocumentException {
        return phrase(textToPrint, 10);
    }

    public static Phrase phrase(String textToPrint, float fontSize) throws IOException, DocumentException {
        return phrase(textToPrint, fontSize, Font.NORMAL);
    }

    public static Phrase phrase(String textToPrint, float fontSize, int fontStyle) throws IOException, DocumentException {
        return new Phrase(textToPrint, font(fontSize, fontStyle));
    }

    public static Font font(float fontSize, int style) throws IOException, DocumentException {
        return FontFactory.getFont("Times-Roman", BaseFont.CP1250, BaseFont.EMBEDDED, fontSize, style);
    }

    public static Font font(float fontSize, int style, BaseColor baseColor) throws IOException, DocumentException {
        return FontFactory.getFont("Times-Roman", BaseFont.CP1250, BaseFont.EMBEDDED, fontSize, style, baseColor);
    }

    public class Builder {
        private String textToPrint;
        private float fontSize;

        public Builder(String textToPrint) {
            this.textToPrint = textToPrint;
        }

        public Builder withFontSize(float fontSize) {
            this.fontSize = fontSize;
        }


    }

}
