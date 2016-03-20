package com.lofatsoftware.mountainquest.pl.generator.page.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;

import java.io.IOException;
import java.util.Optional;

public abstract class PhraseUtil {

    private static final BaseColor DEFAULT_BASE_COLOR = BaseColor.BLACK;

    public static Phrase phrase(String textToPrint) throws IOException, DocumentException {
        return phrase(textToPrint, 10);
    }

    public static Phrase phrase(String textToPrint, float fontSize) throws IOException, DocumentException {
        return phrase(textToPrint, fontSize, Font.NORMAL);
    }

    public static Phrase phrase(String textToPrint, float fontSize, int fontStyle) throws IOException, DocumentException {
        return new Phrase(textToPrint, font(fontSize, fontStyle));
    }

    public static Font font(float fontSize, int fontStyle) throws IOException, DocumentException {
        return FontFactory.getFont("Times-Roman", BaseFont.CP1250, BaseFont.EMBEDDED, fontSize, fontStyle, DEFAULT_BASE_COLOR);
    }

    public class Builder {
        private String textToPrint;
        private Optional<Float> fontSize;
        private Optional<Integer> fontStyle;
        private Optional<BaseColor> fontColor;

        public Builder(String textToPrint) {
            this.textToPrint = textToPrint;
        }

        public Builder withFontSize(float fontSize) {
            this.fontSize = Optional.ofNullable(fontSize);
            return this;
        }

        public Builder withFontStyle(int fontStyle) {
            this.fontStyle = Optional.ofNullable(fontStyle);
            return this;
        }

        public Builder withFontColor(BaseColor fontColor) {
            this.fontColor = Optional.ofNullable(fontColor);
            return this;
        }

        public Phrase build() {
            Font font = FontFactory.getFont(
                    "Times-Roman",
                    BaseFont.CP1250,
                    BaseFont.EMBEDDED,
                    fontSize.orElse(new Float(10)),
                    fontStyle.orElse(Font.NORMAL),
                    fontColor.orElse(DEFAULT_BASE_COLOR));
            return new Phrase(textToPrint, font);

        }

    }

}
