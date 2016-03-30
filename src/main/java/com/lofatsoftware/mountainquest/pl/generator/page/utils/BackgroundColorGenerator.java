package com.lofatsoftware.mountainquest.pl.generator.page.utils;

import com.itextpdf.text.BaseColor;
import com.lofatsoftware.mountainquest.pl.data.Data;

import java.util.Stack;

public class BackgroundColorGenerator {

    public static final BaseColor DEFAULT_BACKGROUND_COLOR = new BaseColor(205, 221, 228);

    private Stack<BaseColor> colors = new Stack<>();
    private String previousMountains = "";
    private BaseColor currentColor;

    {
        colors.add(new BaseColor(216, 208, 255)); // purpule 2
        colors.add(new BaseColor(237, 200, 255)); // purpule
        colors.add(new BaseColor(255, 206, 206)); // pink
        colors.add(new BaseColor(255, 222, 147)); // orange 2
        colors.add(new BaseColor(255, 245, 143)); // yellow-orange
        colors.add(new BaseColor(255, 255, 176)); // yellow light
        colors.add(new BaseColor(238, 255, 196)); // green 2
        colors.add(new BaseColor(190, 255, 163)); // green
        colors.add(new BaseColor(208, 255, 236)); // aquamarine
        colors.add(new BaseColor(208, 231, 255)); // blue 2
        colors.add(new BaseColor(208, 216, 255)); // blue-purpule

        colors.add(new BaseColor(216, 208, 255)); // purpule 2
        colors.add(new BaseColor(237, 200, 255)); // purpule
        colors.add(new BaseColor(255, 206, 206)); // pink
        colors.add(new BaseColor(255, 222, 147)); // orange 2
        colors.add(new BaseColor(255, 245, 143)); // yellow-orange
        colors.add(new BaseColor(255, 255, 176)); // yellow light
        colors.add(new BaseColor(238, 255, 196)); // green 2
        colors.add(new BaseColor(190, 255, 163)); // green
        colors.add(new BaseColor(208, 255, 236)); // aquamarine
        colors.add(new BaseColor(208, 231, 255)); // blue 2
        colors.add(new BaseColor(208, 216, 255)); // blue-purpule
    }

    public BaseColor generate(Data dataItem) {
        if (!previousMountains.equals(dataItem.mountains)) {
            previousMountains = dataItem.mountains;
            currentColor = colors.pop();
        }
        return currentColor;
    }
}
