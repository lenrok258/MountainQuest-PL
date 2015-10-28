package com.lofatsoftware.mountainquest.pl;

import com.lofatsoftware.mountainquest.pl.data.DataImporter;
import com.lofatsoftware.mountainquest.pl.data.Data;
import com.lofatsoftware.mountainquest.pl.generator.ContentGenerator;
import com.lofatsoftware.mountainquest.pl.generator.CoverGenerator;

import java.util.Collections;
import java.util.List;

public class RunMe {

    public static void main(String[] args) throws Exception {

        DataImporter dataImporter = new DataImporter();
        List<Data> dataList = dataImporter.getDataList();
        Collections.sort(dataList);

        ContentGenerator contentGenerator = new ContentGenerator();
        contentGenerator.generatePdf(dataList);

        CoverGenerator coverGenerator = new CoverGenerator();
        coverGenerator.generatePdf();
    }
}
