package com.lofatsoftware.mountainquest.pl.generator;

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
        colors.add(BaseColor.BLUE);
        colors.add(BaseColor.RED);
        colors.add(BaseColor.GREEN);
        colors.add(BaseColor.YELLOW);
        colors.add(BaseColor.CYAN);
        colors.add(BaseColor.DARK_GRAY);
        colors.add(BaseColor.MAGENTA);
        colors.add(BaseColor.GREEN);
        colors.add(BaseColor.ORANGE);
        colors.add(BaseColor.LIGHT_GRAY);
        colors.add(BaseColor.YELLOW);
        colors.add(BaseColor.BLUE);
        colors.add(BaseColor.RED);
        colors.add(BaseColor.GREEN);
        colors.add(BaseColor.YELLOW);
        colors.add(BaseColor.CYAN);
        colors.add(BaseColor.DARK_GRAY);
        colors.add(BaseColor.MAGENTA);
    }

    public BaseColor generate(Data dataItem) {
        if (!previousMountains.equals(dataItem.mountains)) {
            previousMountains = dataItem.mountains;
            currentColor = colors.pop();
        }
        return currentColor;
    }
}
