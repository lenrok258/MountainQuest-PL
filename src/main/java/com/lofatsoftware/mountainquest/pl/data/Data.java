package com.lofatsoftware.mountainquest.pl.data;

public class Data implements Comparable<Data> {

    public String title;
    public String mountains;
    public String height;
    public String latitude;
    public String longitude;
    public String description;
    public String photoUrl;
    public String mapUrl;

    @Override
    public int compareTo(Data o) {
        int i = mountains.compareTo(o.mountains);
        if ( i == 0 ) {
            return title.compareTo(o.title);
        }
        return i;
    }
}

