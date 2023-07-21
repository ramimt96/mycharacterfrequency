package com.example.mycharacterfrequency;

import javafx.scene.canvas.GraphicsContext;
import java.util.Optional;

public class MyPoint {
    double x, y;        //x and y coordinate of point
    MyColor color;      //color of the pixel at the point


    //Constructors
    MyPoint() {
        setPoint(x, y);
        this.color = MyColor.YELLOW;
    }

    MyPoint(double x, double y, MyColor color) {
        setPoint(x, y);
        this.color = Optional.ofNullable(color).orElse(MyColor.YELLOW);
    }

    MyPoint(MyPoint p, MyColor color) {
        setPoint(p);
        this.color = Optional.ofNullable(color).orElse(MyColor.YELLOW);
    }


    //set methods
    public void setPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setPoint(MyPoint p) {
        this.x = p.getX();
        this.y = p.getY();
    }
    public void setColor(MyColor color) {this.color = color;}


    //get methods
    public double getX() {return x;}
    public double getY() {return y;}
    public MyColor getColor() {return color;}


    //shift x and y coordinates
    public void translate(double u, double v) {setPoint(x + u, y + v);}


    //compute distance from point and origin
    public double distanceFromOrigin() {return Math.sqrt(x * x + y * y);}


    //compute distance from point p to this point
    public double distance(MyPoint p) {
        double dx = p.getX() - x;
        double dy = p.getY() - y;
        return Math.sqrt(dx * dx + dy * dy);
    }


    //compute the angle in degrees of the line between this point and MyPoint p
    //measured clockwise with the x-axis
    public double angleX(MyPoint p) {
        double dx = x - p.getX();
        double dy = y - p.getY();
        double angle = Math.toDegrees(Math.atan2(dy, dx));

        return angle >= 0? angle : 360.0 + angle;
    }


    // draw point on canvas
    public void draw(GraphicsContext GC) {
        GC.setFill(color.getJavaFXColor() ) ;
        GC.fillRect(x, y, 1, 1);
    }

    @Override
    public String toString() {return "Point(" + x + ", " + y + ")";}
}
