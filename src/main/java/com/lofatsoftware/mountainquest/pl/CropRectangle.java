package com.lofatsoftware.mountainquest.pl;

public class CropRectangle {

    public float leftReduction;
    public float rightReduction;
    public float topReduction;
    public float bottomReduction;

    public CropRectangle(float left, float right, float top, float bottom) {
        leftReduction = left;
        rightReduction = right;
        topReduction = top;
        bottomReduction = bottom;
    }
}
