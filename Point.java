package com.ball;

import java.awt.*;

//-- Класс описывающий планету
public class Point {
    float x;
    float y;
    float z;
    int maxR;
    Color color;

    public Point(float x, float y, float z, int maxR, Color color) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.maxR = maxR;
        this.color = color;
    }
}
