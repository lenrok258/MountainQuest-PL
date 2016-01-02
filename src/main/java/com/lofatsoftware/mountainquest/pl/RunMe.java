package com.lofatsoftware.mountainquest.pl;

import com.itextpdf.text.DocumentException;
import com.lofatsoftware.mountainquest.pl.data.CoversImporter;
import com.lofatsoftware.mountainquest.pl.data.DataImporter;
import com.lofatsoftware.mountainquest.pl.data.Data;
import com.lofatsoftware.mountainquest.pl.generator.ContentGenerator;
import com.lofatsoftware.mountainquest.pl.generator.CoverGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

public class RunMe {

    public static void main(String[] args) throws Exception {
        generateContentPdf();
        generateCoversPdf();
    }

    private static void generateContentPdf() throws URISyntaxException, DocumentException, IOException, InterruptedException {
        DataImporter dataImporter = new DataImporter();
        List<Data> dataList = dataImporter.getDataList();
        Collections.sort(dataList);

        ContentGenerator contentGenerator = new ContentGenerator(dataList);
        contentGenerator.generatePdf();
    }

    private static void generateCoversPdf() throws Exception {
        List<File> coverFiles = new CoversImporter().getDataList();

        CoverGenerator coverGenerator = new CoverGenerator(coverFiles);
        coverGenerator.generatePdf();
    }
}
