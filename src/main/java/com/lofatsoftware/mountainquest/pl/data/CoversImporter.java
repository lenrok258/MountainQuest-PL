package com.lofatsoftware.mountainquest.pl.data;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

public class CoversImporter {

    private static final String DIR_NAME_DATA = "covers";

    public List<File> getDataList() throws URISyntaxException {
        File rootDirectory = new File(getClass().getResource("/" + DIR_NAME_DATA).toURI());
        return Arrays.asList(rootDirectory.listFiles());
    }

}
