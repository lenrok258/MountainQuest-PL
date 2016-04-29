package com.lofatsoftware.mountainquest.pl;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

import com.lofatsoftware.mountainquest.pl.data.CoversImporter;
import com.lofatsoftware.mountainquest.pl.data.Data;
import com.lofatsoftware.mountainquest.pl.data.DataImporter;
import com.lofatsoftware.mountainquest.pl.generator.ContentGenerator;
import com.lofatsoftware.mountainquest.pl.generator.CoverGenerator;
import com.lofatsoftware.mountainquest.pl.generator.web.WebPageMapGenerator;

public class RunMe {

    public static void main(String[] args) throws Exception {
        generateContentPdf();
        generateCoversPdf();
        generateWebPageMap();
    }

    private static void generateContentPdf() throws Exception {
        List<Data> sortedData = getSortedData();
        ContentGenerator contentGenerator = new ContentGenerator(sortedData);
        contentGenerator.generatePdf();
    }

    private static void generateCoversPdf() throws Exception {
        List<File> coverFiles = new CoversImporter().getDataList();

        CoverGenerator coverGenerator = new CoverGenerator(coverFiles);
        coverGenerator.generatePdf();
    }

    private static void generateWebPageMap() throws URISyntaxException, IOException {
        List<Data> sortedData = getSortedData();
        new WebPageMapGenerator(sortedData).generateHtml();
    }

    private static List<Data> getSortedData() throws URISyntaxException {
        DataImporter dataImporter = new DataImporter();
        List<Data> dataList = dataImporter.getDataList();
        Collections.sort(dataList);
        return dataList;
    }
}
