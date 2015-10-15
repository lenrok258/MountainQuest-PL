package com.lofatsoftware.mountainquest.pl.data;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DataImporter {

    private static final String MAP_URL_TEMPLATE = "http://maps.googleapis.com/maps/api/staticmap?scale=2&size=400x300&maptype=roadmap&format=png&visual_refresh=false&markers=icon:http://lofatsoftware.com/hotlinking/002/gmap-marker.png|color:0xff0000|{0},{1}&visible=Krak%C3%B3w&visible=49.100821,19.994676";
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
            File[] files = directory.listFiles((current, name) -> {
                return FILE_NAME_DATA.equals(name);
            });
            if (files.length == 1) {
                FileReader fileReader = new FileReader(files[0]);
                Data data = gson.fromJson(fileReader, Data.class);
                if (data.photoUrl == null) {
                    data.photoUrl = computePhotoUrl(directory);
                }
                data.mapUrl = computeMapUrl(data.longitude, data.latitude);
                return data;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String computePhotoUrl(File directory) {
        File[] files = directory.listFiles((current, name) -> name.endsWith(".png") || name.endsWith(".jpg"));
        return (files.length > 0) ? files[0].getAbsolutePath() : null;
    }

    private String computeMapUrl(String longitude, String latitude) {
        return MessageFormat.format(MAP_URL_TEMPLATE, latitude, longitude);
    }


}
