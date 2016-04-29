package com.lofatsoftware.mountainquest.pl.generator.web;

import com.itextpdf.text.BaseColor;
import com.lofatsoftware.mountainquest.pl.data.Data;
import com.lofatsoftware.mountainquest.pl.generator.page.utils.BackgroundColorGenerator;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class WebPageMapGenerator {

    private static final String FILEPATH_HTML_TEMPLATE = "/web/index-template.html";
    private static final String PATH_HTML_OUTPUT = "/web";
    private static final String FILENAME_HTML_OUTPUT = "index.html";

    private static final String CS_MARKER_SVG_ICON =
            "var markerSvgIcon###i### = {" +
                    "path: 'M 409.81,160.113 C 409.79,71.684 338.136,0 249.725,0 161.276,0 89.583,71.684 89.583,160.113 c 0,76.325 119.274,280.238 151.955,334.638 1.72,2.882 4.826,4.641 8.178,4.641 3.351,0 6.468,-1.759 8.168,-4.631 32.661,-54.4 151.926,-258.323 151.926,-334.648 z'," +
                    "fillColor: '###COLOR###'," +
                    "fillOpacity: 1.0," +
                    "scale: 0.08," +
                    "strokeColor: 'black'," +
                    "strokeWeight: 2," +
                    "anchor: new google.maps.Point(0, 250)" +
                    "};";

    private static final String HTML_INFOWINDOW_CONTENT_TEMPLATE =
            "<h2>###SHELTER-NAME###</h2>" +
                    "Ogległość: <b>###SHELTER-DISTANCE###km</b>, numer strony: <b>###PAGE-NUMBER###</b><br/><br/>" +
                    "###SHELTER-DESCRIPTION###<br/><br/>" +
                    "<a href=\"https://pl.wikipedia.org/wiki/###SHELTER-NAME###\">###SHELTER-NAME### na Wikipedia</a>" +
                    "<br/><br/>";

    private static final String JS_INFOWINDOW_TEMPLATE =
            "var infowindow###i### = new google.maps.InfoWindow({" +
                    "content: '###INFO-WINDOW-CONTENT###'" +
                    "});";

    private static final String JS_MARKER_TEMPLATE =
            "var marker###i### = new google.maps.Marker({" +
                    "position: {lat: ###LATITUDE###, lng: ###LONGITUDE###}," +
                    "map: map," +
                    "icon: markerSvgIcon###i###," +
                    "title: 'Shelter'," +
                    "animation: google.maps.Animation.DROP" +
                    "});" +
                    "marker###i###.addListener('click', function() { infowindow###i###.open(map, marker###i###); });";

    private List<Data> dataList;
    private BackgroundColorGenerator colorGenerator = new BackgroundColorGenerator();

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
            String htmlInfoWindowCode = HTML_INFOWINDOW_CONTENT_TEMPLATE
                    .replace("###SHELTER-NAME###", data.title)
                    .replace("###SHELTER-DISTANCE###", data.distanceFromCracow)
                    .replace("###PAGE-NUMBER###", String.valueOf(i + 1))
                    .replace("###SHELTER-DESCRIPTION###", description);

            String jsInfoWindowCode = JS_INFOWINDOW_TEMPLATE
                    .replace("###INFO-WINDOW-CONTENT###", htmlInfoWindowCode)
                    .replace("###i###", String.valueOf(i));

            String jsMarkerSvgIcon = CS_MARKER_SVG_ICON
                    .replace("###i###", String.valueOf(i))
                    .replace("###COLOR###", getColor(data));


            String jsMarkerCode = JS_MARKER_TEMPLATE
                    .replace("###LATITUDE###", data.latitude)
                    .replace("###LONGITUDE###", data.longitude)
                    .replace("###i###", String.valueOf(i));

            jsMarkersCode += (jsInfoWindowCode + "\n");
            jsMarkersCode += (jsMarkerSvgIcon + "\n");
            jsMarkersCode += (jsMarkerCode + "\n");
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
        FileUtils.write(outputFile, html, "UTF-8");
    }

    public String getColor(Data data) {
        BaseColor baseColor = colorGenerator.generate(data);
        int red = baseColor.getRed();
        int green = baseColor.getGreen();
        int blue = baseColor.getBlue();
        String colorHex = String.format("#%02x%02x%02x", red, green, blue);
        System.out.println(colorHex);
        return colorHex;
    }
}
