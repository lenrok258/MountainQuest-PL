package com.lofatsoftware.mountainquest.pl.data;

import java.text.Collator;
import java.util.Locale;

public class Data implements Comparable<Data> {

    private static Collator collator = Collator.getInstance(new Locale("pl", "PL"));

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
        int compareResult = collator.compare(mountains, o.mountains);
        if (compareResult == 0) {
            return collator.compare(title, o.title);
        }
        return compareResult;
    }
}

