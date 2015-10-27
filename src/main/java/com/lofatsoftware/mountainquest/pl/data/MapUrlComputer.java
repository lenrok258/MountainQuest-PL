package com.lofatsoftware.mountainquest.pl.data;


import java.io.File;
import java.text.MessageFormat;

public class MapUrlComputer {

    private static final String MAP_URL_TEMPLATE = "http://maps.googleapis.com/maps/api/staticmap?scale=2&size=400x300&maptype=roadmap&format=png&visual_refresh=false&markers=icon:http://lofatsoftware.com/hotlinking/002/gmap-marker.png|color:0xff0000|{0},{1}&visible=Krak%C3%B3w&visible=49.100821,19.994676";

    private File dataDirectory;
    private String longitude;
    private String latitude;

    public MapUrlComputer(File dataDirectory, String longitude, String latitude) {
        this.dataDirectory = dataDirectory;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getUrl() {
        return computeGoogleMapUrl(this.latitude, this.longitude);
    }

    private String computeGoogleMapUrl(String longitude, String latitude) {
        return MessageFormat.format(MAP_URL_TEMPLATE, latitude, longitude);
    }

}
