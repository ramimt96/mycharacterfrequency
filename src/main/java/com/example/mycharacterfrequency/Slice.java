package com.example.mycharacterfrequency;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;


public class Slice {
    MyPoint center;                 //center point of pie
    double width, height;           //width and height of pie
    double startAngle;              //starting angle of arc in degrees
    double arcAngle;                //angular extension in degrees of the arc
    MyColor color;                  //color of slice; randomized
    String information;             //key and value information of slice

    //Constructors
    Slice(MyPoint center, double width, double height, double startAngle, double arcAngle, MyColor color, String information) {
        this.center = center;
        this.width = width;
        this.height = height;
        this.startAngle = startAngle;
        this.arcAngle = arcAngle;
        this.color = color;
        this.information = information;
    }

    Slice(Slice s) {
        this.center = s.getCenter();
        this.width = s.getWidth();
        this.height = s.getHeight();
        this.startAngle = s.getStartAngle();
        this.arcAngle = s.getArcAngle();
        this.information = s.getInformation();
    }

    //Set methods
    public void setColor(MyColor color) {this.color = color;}

    //Get methods
    public MyPoint getCenter() {return center;}
    public double getWidth() {return width;}
    public double getHeight() {return height;}
    public double getStartAngle() {return startAngle;}
    public double getArcAngle() {return arcAngle;}
    public String getInformation() {return information;}

    //Draw a Slice
    public void draw(GraphicsContext GC) {
        double x = center.getX(); double y = center.getY();
        double a = 0.55 * width; double b = 0.55 * height;
        double midAngle = startAngle + 0.5 * arcAngle;
        midAngle = midAngle < 360 ? midAngle : midAngle - 360;

        GC.setFill(color.getJavaFXColor());
        GC.fillArc(x - 0.5 * width, y - 0.5 * height, width, height,
                startAngle, arcAngle, ArcType.ROUND);

        GC.setStroke(MyColor.WHITE.getJavaFXColor());
        GC.strokeArc(x - 0.5 * width, y - 0.5 * height, width, height,
                startAngle, arcAngle, ArcType.ROUND);

        double u = a * Math.abs(Math.sin(Math.toRadians(midAngle)));
        double v = a * Math.abs(Math.cos(Math.toRadians(midAngle)));
        double w = 1.0 / Math.sqrt(u * u + v * v);

        u = (midAngle >= 0.0 && midAngle <= 180.0) ? -u : u;
        v = (midAngle >= 90.0 && midAngle <= 270.0) ? -v : v;
        double xText = x + a * v * w;
        double yText = y + b * u * w;

        xText = (midAngle >= 90.0 && midAngle <= 270.0) ? xText - 0.125 * width : xText;

        GC.setStroke(MyColor.LINEN.getJavaFXColor());
        GC.setFont(Font.font("Calibri", 13));
        GC.strokeText(information, xText, yText);
    }

    @Override
    public String toString(){
        return "Slice: Center (" + center.getX() + ", " + center.getY() + ")"
                + " Width " + width + " Height " + height
                + " (Starting Angle, Angle): (" + startAngle + ", " + arcAngle + "),"
                + " Color " + color.getJavaFXColor();
    }
}
