package com.example.mycharacterfrequency;

import javafx.scene.canvas.GraphicsContext;
import java.util.Optional;


public class MyLine {

    MyPoint p1, p2;                     // endpoints of the line
    MyPoint [] pLine = new MyPoint[2];  // Array of type MyPoint holding the endpoints of the line
    MyColor color;                      // Color of MyLine object of type enum MyColor

    // Constructors
    MyLine(MyPoint p1, MyPoint p2, MyColor color) {
        this.p1 = p1;
        this.p2 = p2;
        pLine[0] = p1;
        pLine[1] = p2;
        this.color = Optional.ofNullable(color).orElse(MyColor.YELLOW);
    }

    MyLine(MyLine L, MyColor color) {
        this.p1 = (L.getLine())[0];
        this.p1 = (L.getLine())[1];
        pLine[0] = p1;
        pLine[1] = p2;
        this.color = Optional.ofNullable(color).orElse(L.getColor());
    }

    //Set methods
    public void setColor(MyColor color) {this.color = color;}

    //Get methods
    public MyPoint [] getLine() {return pLine;}
    public MyColor getColor() {return color;}

    //angle in degrees with the x-axis
    public double angleX() {return p1.angleX(p2);}

    //Length
    public double length() {return p1.distance(p2);}


    public double perimeter() {return length();}
    public double area() {return 0;}


    // Draw a MyLine object
    public void stroke (GraphicsContext GC) {
        GC.setStroke(color.getJavaFXColor());
        GC.strokeLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }
    public void draw(GraphicsContext GC) {
        GC.setStroke(color.getJavaFXColor());
        GC.strokeLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }

    @Override
    public String toString() {
        return "Line [" + p1 + ", " + p2 + "] Length " + length();
    }
}