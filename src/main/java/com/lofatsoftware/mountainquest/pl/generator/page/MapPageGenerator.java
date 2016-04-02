package com.lofatsoftware.mountainquest.pl.generator.page;

import com.itextpdf.text.pdf.PdfPTable;
import com.lofatsoftware.mountainquest.pl.data.Data;

import java.text.MessageFormat;
import java.util.List;

/*
 * Not used (readability is awful)
 */
public class MapPageGenerator implements PageGenerator {

    private static final String MAP_POINTER_URL = "http://lofatsoftware.com/hotlinking/002/2/1.png";
    private static final String MAP_MARKER = "|{0},{1}";
    private static final String MAP_URL = "http://maps.googleapis.com/maps/api/staticmap?scale=2&size=640x452&maptype=terrain&visible=Krak%C3%B3w&visible=49.100821,19.994676&markers=icon:" + MAP_POINTER_URL;

    private final List<Data> dataList;

    public MapPageGenerator(List<Data> dataList) {
        this.dataList = dataList;
    }

    @Override
    public PdfPTable generatePage() throws Exception {
        String mapUrl = computeMapUrl(dataList);
        System.out.println(mapUrl);
        return null;
    }

    private String computeMapUrl(List<Data> dataList) {
        String mapPointers = dataList.stream()
                .map(data -> MessageFormat.format(MAP_MARKER, data.latitude, data.longitude))
                .reduce((result, element) -> result += element)
                .get();

        return MAP_URL + mapPointers;
    }

}
