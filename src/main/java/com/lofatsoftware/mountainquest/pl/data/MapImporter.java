package com.lofatsoftware.mountainquest.pl.data;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.util.Optional;

public class MapImporter {

    public static final String MAP_URL_TEMPLATE = "http://maps.googleapis.com/maps/api/staticmap?scale=2&size=400x300&maptype=roadmap&format=png&visual_refresh=false&markers=icon:http://lofatsoftware.com/hotlinking/002/gmap-marker-blue-dark.png|{0},{1}&visible=Krak%C3%B3w&visible=49.100821,19.994676";
    public static final String MAP_LOCAL_FILE_NAME = "map.png-cache";

    private File dataDirectory;
    private String longitude;
    private String latitude;

    public MapImporter(File dataDirectory, String longitude, String latitude) {
        this.dataDirectory = dataDirectory;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getUrl() {
        String filePath = getLocalMapFileUrl()
                .orElseGet(this::downloadGoogleMapImage);
        return "file://" + filePath;
    }

    private Optional<String> getLocalMapFileUrl() {
        File[] files = dataDirectory.listFiles((dir, name) -> {
            return MAP_LOCAL_FILE_NAME.equals(name);
        });
        return files.length == 1
                ? Optional.of(files[0].getAbsolutePath())
                : Optional.empty();
    }

    private String downloadGoogleMapImage() {
        try {
            System.out.println("Downloading map image from Google to " + dataDirectory.getPath());
            URL googleMapsUrl = getGoogleMapsUrl(longitude, latitude);

            Path localFilePath = localMapFilePath(dataDirectory);

            try (InputStream in = googleMapsUrl.openStream()) {
                Files.copy(in, localFilePath, StandardCopyOption.REPLACE_EXISTING);
            }
            return localFilePath.toAbsolutePath().toString();
        } catch (Exception e) {
            throw new IllegalStateException("Unable to download map image", e);
        }
    }

    private URL getGoogleMapsUrl(String longitude, String latitude) throws MalformedURLException {
        String urlString = MessageFormat.format(MAP_URL_TEMPLATE, latitude, longitude);
        return new URL(urlString);
    }

    private Path localMapFilePath(File dataDirectory) {
        String pathString = dataDirectory.getAbsolutePath() + File.separatorChar + MAP_LOCAL_FILE_NAME;
        return Paths.get(pathString);
    }

}
