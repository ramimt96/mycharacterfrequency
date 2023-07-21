package com.example.mycharacterfrequency;

import javafx.scene.canvas.GraphicsContext;
import java.util.Optional;


public class MyCircle extends MyShape{
    MyPoint center;
    double radius;
    MyColor color;

    //Constructors
    MyCircle(MyPoint p, double radius, MyColor color) {
        //super(p, 2.0 * radius, 2.0 * radius, color);
        super(p, color);
        this.center = p;
        this.radius = radius;
        this.color = Optional.ofNullable(color).orElse(MyColor.YELLOW);
    }

    MyCircle(MyCircle C, MyColor color) {
        super(C.p, color);
        this.center = C.getCenter();
        this.radius = C.getRadius();
        this.color = Optional.ofNullable(color).orElse(MyColor.YELLOW);
    }

    //set methods
    @Override
    public void setColor(MyColor color) {this.color = color;}

    //get methods
    public MyPoint getCenter() {return center;}
    public double getRadius() {return radius;}
    @Override
    public MyColor getColor() {return color;}

    //override abstract methods in MyShape
    @Override
    public double perimeter() {return 2 * Math.PI * radius;}
    @Override
    public double area() {return Math.PI * radius * radius;}
    @Override
    public void stroke(GraphicsContext GC) {
        GC.setStroke(color.getJavaFXColor());
        GC.strokeOval(center.getX() - radius, center.getY() - radius, 2 * radius, 2 * radius);
    }
    @Override
    public void draw(GraphicsContext GC) {
        GC.setFill(color.getJavaFXColor());
        GC.fillOval(center.getX() - radius, center.getY() - radius, 2 * radius, 2 * radius);
    }

    //override abstract methods in MyShapeInterface
    @Override
    public MyRectangle getMyBoundingRectangle() {
        double x = center.getX();
        double y = center.getY();
        double width = 2 * radius;
        double height = 2 * radius;

        MyPoint topLeft = new MyPoint(x - radius, y - radius, color);
        return new MyRectangle(topLeft, width, height, color);
    }

    @Override
    public boolean pointInMyShape(MyPoint p) {
        double x = p.getX();
        double y = p.getY();
        double a = radius;
        double b = radius;

        return ((x - center.getX()) * (x - center.getX())) / (a * a)
                + ((y - center.getY()) * (y - center.getY())) / (b * b) <= 1;
    }

    @Override
    public boolean similarObject(MyShape S) {
        if (S.getClass().toString().equals("class MyCircle")) {
            MyCircle C = (MyCircle) S;
            return radius == C.getRadius();
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Circle Center: {" + center.getX() + ", " + center.getY() + ")" + "\n" +
                "Radius: " + radius + "\n" +
                "Perimeter: " + perimeter() + "\n" +
                "Area:" + area();
    }
}