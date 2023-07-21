package com.example.mycharacterfrequency;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;


public class Bar {
    MyPoint pTLC;
    double widthBar, heightBar;
    MyColor color;
    String information;

    //Constructors
    Bar(MyPoint p, double widthBar, double heightBar, MyColor color, String information) {
        this.pTLC = p;
        this.widthBar = widthBar;
        this.heightBar = heightBar;
        this.color = color;
        this.information = information;
    }

    Bar(Bar b) {
        this.pTLC = b.getPTLC();
        this.widthBar = b.getWidthBar();
        this.heightBar = b.getHeightBar();
        this.information = b.getInformation();
    }

    //Set methods
    public void setColor(MyColor color) {this.color = color;}

    //Get methods
    public MyPoint getPTLC() {return pTLC;}
    public double getWidthBar() {return widthBar;}
    public double getHeightBar() {return heightBar;}
    public String getInformation() {return information;}

    //Draw a Bar object
    public void draw(GraphicsContext GC) {
        double x = pTLC.getX();
        double y = pTLC.getY();

        GC.setFill(color.getJavaFXColor());
        GC.fillRect(x, y, widthBar, heightBar);
        GC.setStroke(MyColor.WHITE.getJavaFXColor());
        GC.strokeRect(x, y, widthBar, heightBar);

        double xText = x + 0.5 * widthBar;
        double yText = y * 0.975;

        GC.translate(xText, yText);
        GC.rotate(-90.0);
        GC.setStroke(MyColor.WHITE.getJavaFXColor());
        GC.setFont(Font.font("Calibri", 13));
        GC.strokeText(information, 0, 0);

        GC.rotate(90.0);
        GC.translate(-xText, -yText);
    }

    @Override
    public String toString() {
        return "Bar: Top Left Corner(" + pTLC.getX() + ", " + pTLC.getY() + ") Width " + widthBar + " Height "
                + heightBar + " Color " + color.getJavaFXColor();
    }
}
