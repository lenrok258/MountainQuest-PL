package com.lofatsoftware.mountainquest.pl.generator.page.tiles;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPCell;

import java.io.IOException;

interface TileGenerator {

    PdfPCell generateTile() throws Exception;

}
