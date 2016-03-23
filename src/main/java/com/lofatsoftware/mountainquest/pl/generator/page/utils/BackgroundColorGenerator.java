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
        //TODO: Put real colors
        colors.add(BaseColor.RED);
        colors.add(BaseColor.RED);
        colors.add(BaseColor.RED);
        colors.add(BaseColor.RED);
        colors.add(BaseColor.RED);
        colors.add(BaseColor.RED);
        colors.add(BaseColor.RED);
        colors.add(BaseColor.RED);
        colors.add(BaseColor.RED);
        colors.add(BaseColor.RED);
        colors.add(BaseColor.RED);
        colors.add(BaseColor.RED);
        colors.add(BaseColor.RED);
        colors.add(BaseColor.RED);
        colors.add(BaseColor.RED);
        colors.add(BaseColor.RED);
        colors.add(BaseColor.RED);
        colors.add(BaseColor.RED);
        colors.add(BaseColor.RED);

        colors.add(new BaseColor(255, 255, 176)); // yellow
        colors.add(new BaseColor(255, 206, 206)); // pink
        colors.add(new BaseColor(255, 236, 143)); // yellow-orange
        colors.add(new BaseColor(184, 243, 243)); // blue
        colors.add(new BaseColor(190, 255, 163)); // green
        colors.add(new BaseColor(237, 200, 255)); // purpule
    }

    public BaseColor generate(Data dataItem) {
        if (!previousMountains.equals(dataItem.mountains)) {
            previousMountains = dataItem.mountains;
            currentColor = colors.pop();
        }
        return currentColor;
    }
}
