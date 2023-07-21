package com.example.mycharacterfrequency;

import javafx.scene.canvas.GraphicsContext;
import java.util.Optional;
public abstract class MyShape implements MyShapeInterface {
    MyPoint p;
    MyColor color;

    //Constructors
    MyShape(MyPoint p, MyColor color) {
        setPoint(p);
        this.color = Optional.ofNullable(color).orElse(MyColor.YELLOW);
    }

    MyShape(double x, double y, MyColor color) {
        setPoint(x, y);
        this.color = Optional.ofNullable(color).orElse(MyColor.YELLOW);
    }


    //set methods
    public void setPoint(MyPoint p) {this.p = p;}
    public void setPoint(double x, double y) {p.setPoint(x, y);}
    public void setColor(MyColor color) {this.color = color;}

    //get methods
    public MyPoint getPoint() {return p;}
    public MyColor getColor() {return color;}


    // Abstract methods -- must be overridden in all subclasses extending MyShape
    public abstract double perimeter();
    public abstract double area();
    public abstract void stroke(GraphicsContext GC);
    public abstract void draw(GraphicsContext GC);


    @Override
    public String toString() {return "This is MyShape Object";}
}


