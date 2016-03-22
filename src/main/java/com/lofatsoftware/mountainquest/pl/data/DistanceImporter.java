package com.lofatsoftware.mountainquest.pl.data;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

public class DistanceImporter {

    private static final String DISTANCE_API_URL_TEMPLATE =
            "https://maps.googleapis.com/maps/api/distancematrix/json?origins=50.0768693,19.9028439&destinations={0},{1}";
    private static final String DISTANCE_LOCAL_FILE_NAME = "distance.json";

    private final File dataDirectory;
    private final String longitude;
    private final String latitude;

    public DistanceImporter(File dataDirectory, String longitude, String latitude) {
        this.dataDirectory = dataDirectory;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getDistanceFromCracow() throws IOException {
        return getLocalFileDistanceValue()
                .orElseGet(() -> downloadDistanceToFileAndReturn());
    }

    private Optional<String> getLocalFileDistanceValue() throws IOException {
        File[] files = dataDirectory.listFiles((dir, name) -> {
            return DISTANCE_LOCAL_FILE_NAME.equals(name);
        });
        return files.length == 1
                ? Optional.of(readValueFromFile(files[0].getAbsolutePath()))
                : Optional.empty();
    }

    private String downloadDistanceToFileAndReturn() {
        try {
            String jsonPayload = downloadDistanceJsonWithRetries();

            return "trolo";
           /* Path localFilePath = localDistanceFilePath(dataDirectory);


            try (InputStream in = distanceUrl.openStream()) {
                Files.copy(in, localFilePath, StandardCopyOption.REPLACE_EXISTING);
            }
            return readValueFromFile(localFilePath.toAbsolutePath().toString());*/
        } catch (Exception e) {
            throw new IllegalStateException("Unable to download distance", e);
        }
    }

    private String downloadDistanceJsonWithRetries() throws IOException {
        for (int i = 0; i < 10; i++) {
            float newLatitude = Float.parseFloat(latitude) + (0.01f * i);
            String jsonPayload = downloadDistanceJson(String.valueOf(newLatitude), longitude);
            JSONObject jsonObject = new JSONObject(jsonPayload);
            if (hasCorrectResults(jsonObject)) {
                return jsonPayload;
            }
        }
        throw new IllegalStateException("Unable to find distance");
    }

    private String downloadDistanceJson(String latitude, String longitude) throws IOException {
        System.out.println("Downloading distance from Google API to " + dataDirectory.getPath());
        URL distanceUrl = getDistanceAPIUrl(latitude, longitude);
        return IOUtils.toString(distanceUrl.openStream());
    }

    private URL getDistanceAPIUrl(String latitude, String longitude) throws MalformedURLException {
        String urlString = MessageFormat.format(DISTANCE_API_URL_TEMPLATE, latitude, longitude);
        System.out.println(urlString);
        return new URL(urlString);
    }

    private Path localDistanceFilePath(File dataDirectory) {
        String pathString = dataDirectory.getAbsolutePath() + File.separatorChar + DISTANCE_LOCAL_FILE_NAME;
        return Paths.get(pathString);
    }

    private String readValueFromFile(String absolutePath) throws IOException {
        URI uri = new File(absolutePath).toURI();
        String fileContent = new String(Files.readAllBytes(Paths.get(uri)));

        JSONObject jsonObject = new JSONObject(fileContent);
        if (hasCorrectResults(jsonObject)) {

        }

        return getDistanceValue(jsonObject);
    }

    private boolean hasCorrectResults(JSONObject jsonObject) {
        return !jsonObject
                .getJSONArray("rows").getJSONObject(0)
                .getJSONArray("elements").getJSONObject(0)
                .getString("status").equals("ZERO_RESULTS");
    }

    private String getDistanceValue(JSONObject jsonObject) {
        int meters = jsonObject
                .getJSONArray("rows").getJSONObject(0)
                .getJSONArray("elements").getJSONObject(0)
                .getJSONObject("distance").getInt("value");
        return String.valueOf(meters / 1000);
    }
}
