package com.lofatsoftware.mountainquest.pl.generator.page.utils;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class ImageUtils {

    public static Image cropImageToRectangle(PdfWriter pdfWriter, Image image, CropRectangle cropRectangle) throws
            DocumentException {
        return cropImage(pdfWriter, image, cropRectangle);
    }

    public static Image cropImageToMeetRatio(PdfWriter pdfWriter, Image image, float heightToWidthRatio) throws
            DocumentException {
        float imageWidth = image.getScaledWidth();
        float imageHeight = image.getScaledHeight();
        float imageDesiredWidth = imageHeight / heightToWidthRatio;
        float widthToCut = imageWidth - imageDesiredWidth; //TODO: Sprawdzic czy liczba nie jest ujemna. Jeżeli tak to przycinać wysokość zamiast szerokości

        return cropImage(pdfWriter, image, new CropRectangle(widthToCut / 2, widthToCut / 2, 0, 0));
    }

    private static Image cropImage(PdfWriter pdfWriter, Image image, CropRectangle cropRectangle) throws
            DocumentException {
        float width = image.getScaledWidth();
        float height = image.getScaledHeight();
        PdfTemplate template = pdfWriter.getDirectContent().createTemplate(
                width - cropRectangle.leftReduction - cropRectangle.rightReduction,
                height - cropRectangle.topReduction - cropRectangle.bottomReduction);
        template.addImage(image,
                width, 0, 0,
                height, -cropRectangle.leftReduction, -cropRectangle.bottomReduction);
        return Image.getInstance(template);
    }

}
