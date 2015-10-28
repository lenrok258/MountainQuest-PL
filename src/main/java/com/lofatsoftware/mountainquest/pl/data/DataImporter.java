package com.lofatsoftware.mountainquest.pl.data;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DataImporter {

    private static final String DIR_NAME_DATA = "data";
    private static final String FILE_NAME_DATA = "dane.json";

    private List<Data> dataList;
    private Gson gson = new Gson();

    public List<Data> getDataList() throws URISyntaxException {
        if (dataList == null) {
            dataList = loadData();
        }
        return dataList;
    }

    private List<Data> loadData() throws URISyntaxException {
        List<File> dirs = listDirectories();
        return dirs.stream()
                .map(this::loadDataFromDirectory)
                .filter(data -> data != null)
                .collect(Collectors.toList());
    }

    private List<File> listDirectories() throws URISyntaxException {
        File rootDirectory = new File(getClass().getResource("/" + DIR_NAME_DATA).toURI());
        File[] files = rootDirectory.listFiles((current, name) -> {
            return current.isDirectory();
        });
        return Arrays.asList(files);
    }

    private Data loadDataFromDirectory(File directory) {
        try {
            File[] files = directory.listFiles((dir, name) -> {
                return FILE_NAME_DATA.equals(name);
            });
            if (files.length == 1) {
                FileReader fileReader = new FileReader(files[0]);
                Data data = gson.fromJson(fileReader, Data.class);
                if (data.photoUrl == null) {
                    data.photoUrl = computePhotoUrl(directory);
                }

                MapImporter mapImporter = new MapImporter(directory, data.longitude, data.latitude);
                data.mapUrl = mapImporter.getUrl();

                return data;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String computePhotoUrl(File directory) {
        File[] files = directory.listFiles((current, name) ->
                name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".PNG") || name.endsWith(".JPG"));
        return (files.length > 0) ? files[0].getAbsolutePath() : null;
    }




}
