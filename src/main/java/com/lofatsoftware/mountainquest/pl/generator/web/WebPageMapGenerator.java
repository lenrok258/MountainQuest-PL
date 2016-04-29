package com.lofatsoftware.mountainquest.pl.generator.web;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.lofatsoftware.mountainquest.pl.data.Data;

public class WebPageMapGenerator {

    private static final String FILEPATH_HTML_TEMPLATE = "/web/index-template.html";
    private static final String PATH_HTML_OUTPUT = "/web";
    private static final String FILENAME_HTML_OUTPUT = "index.html";

    private static final String HTML_INFOWINDOW_CONTENT_TEMPLATE =
            "<h2>###SHELTER-NAME###</h2>" +
            "Distance: ###SHELTER-DISTANCE###km<br/><br/>" +
            "###SHELTER-DESCRIPTION###<br/><br/>";

    private static final String JS_INFOWINDOW_TEMPLATE =
            "var infowindow###i### = new google.maps.InfoWindow({" +
                "content: '###INFO-WINDOW-CONTENT###'" +
            "});";

    private static final String JS_MARKER_TEMPLATE =
            "var marker###i### = new google.maps.Marker({" +
                    "position: {lat: ###LATITUDE###, lng: ###LONGITUDE###}," +
                    "map: map," +
                    "icon: 'img/marker2.png'," +
                    "title: 'Shelter'," +
                    "animation: google.maps.Animation.DROP" +
                    "});" +
            "marker###i###.addListener('click', function() { infowindow###i###.open(map, marker###i###); });";

    private List<Data> dataList;

    public WebPageMapGenerator(List<Data> dataList) {
        this.dataList = dataList;
    }

    public void generateHtml() throws URISyntaxException, IOException {
        String jsMarkers = generateMarkerJSCode(dataList);
        String html = generateHtmlCode(jsMarkers);
        saveHtmlFile(html);
    }

    private String generateMarkerJSCode(List<Data> dataList) {
        String jsMarkersCode = "";
        for (int i = 0; i < dataList.size(); i++) {
            Data data = dataList.get(i);

            String description = data.description.replace("\"", "").replace("'", "");
            String htmlInfoWindowCode = HTML_INFOWINDOW_CONTENT_TEMPLATE.replace("###SHELTER-NAME###", data.title);
            htmlInfoWindowCode = htmlInfoWindowCode.replace("###SHELTER-DISTANCE###", data.distanceFromCracow);
            htmlInfoWindowCode = htmlInfoWindowCode.replace("###SHELTER-DESCRIPTION###", description);

            String jsInfoWindowCode = JS_INFOWINDOW_TEMPLATE.replace("###INFO-WINDOW-CONTENT###", htmlInfoWindowCode);
            jsInfoWindowCode = jsInfoWindowCode.replace("###i###", String.valueOf(i));

            String jsMarkerCode = JS_MARKER_TEMPLATE.replace("###LATITUDE###", data.latitude);
            jsMarkerCode = jsMarkerCode.replace("###LONGITUDE###", data.longitude);
            jsMarkerCode = jsMarkerCode.replace("###i###", String.valueOf(i));

            jsMarkersCode += jsInfoWindowCode;
            jsMarkersCode += '\n';
            jsMarkersCode += jsMarkerCode;
            jsMarkersCode += '\n';
        }
        return jsMarkersCode;
    }

    private String generateHtmlCode(String jsMarkers) throws URISyntaxException, IOException {
        File templateFile = new File(getClass().getResource(FILEPATH_HTML_TEMPLATE).toURI());
        String templateContent = FileUtils.readFileToString(templateFile);
        String html = templateContent.replace("'###MARKERS###'", jsMarkers);
        System.out.println(html);
        return html;
    }

    private void saveHtmlFile(String html) throws URISyntaxException, IOException {
        File outputPath = new File(getClass().getResource(PATH_HTML_OUTPUT).toURI());
        File outputFile = new File(outputPath, FILENAME_HTML_OUTPUT);
        /*if (!outputFile.exists()) {
            outputFile.createNewFile();
        } else {
            outputFile.
        }*/

        FileUtils.write(outputFile, html, "UTF-8");
    }
}
