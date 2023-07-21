package com.example.mycharacterfrequency;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

interface MyShapeInterface {
    //Abstract methods
    //return bounding rectangle of objects in "MyClass" hierarchy
    MyRectangle getMyBoundingRectangle();

    //returns true if MyPoint object is located in MyShape object
    boolean pointInMyShape(MyPoint p);

    //returns true if two MyShape objects are similar
    boolean similarObject(MyShape S);
    
    
    //Static methods
    //return true if two MyShape objects are similar
    static boolean similarObjects(MyShape S1, MyShape S2){
        String sClassS1 = S1.getClass().toString();
        String sClassS2 = S2.getClass().toString();
        
        if (sClassS1.equals(sClassS2)){
            switch (sClassS1) {

                case "class MyRectangle":
                    MyRectangle R1 = (MyRectangle) S1;
                    MyRectangle R2 = (MyRectangle) S2;
                    return R1.getWidth() == R2.getWidth() && R1.getHeight() == R2.getHeight();

                case "class MyCircle":
                    MyCircle C1 = (MyCircle) S1;
                    MyCircle C2 = (MyCircle) S2;
                    return C1.getRadius() == C2.getRadius();

                default:
                    // the classes of the two shapes are not included in the MyShape class hierarchy
                    return false;
            }
        } else {
            // MyShape objects, S1 and S2, are not instances of the same class
            return false;
        }
    }

    //return intersection of two MyRectangle objects if they overlap, null otherwise
    static MyRectangle overlapMyRectangles(MyRectangle R1, MyRectangle R2){
        double x1 = R1.getTLC().getX();
        double y1 = R1.getTLC().getY();
        double w1 = R1.getWidth();
        double h1 = R1.getHeight();

        double x2 = R2.getTLC().getX();
        double y2 = R2.getTLC().getY();
        double w2 = R2.getWidth();
        double h2 = R2.getHeight();

        //no overlap; one object above the other; return null
        if (y1 + h1 < y2  || y1 > y2 + h2) return null;

        //no overlap; one object next to the other; return null
        if (x1 + w1 < x2  || x1 > x2 + w2) return null;

        //overlap exists; return intersection
        double xmax = Math.max(x1, x2);
        double ymax = Math.max(y1, y2);
        double xmin = Math.min(x1+w1, x2+w2);
        double ymin = Math.min(y1+h1, y2+h2);

        MyPoint p = new MyPoint(xmax, ymax, null);
        return new MyRectangle(p, Math.abs(xmax - xmin), Math.abs(ymax - ymin), null);
    }


    //return the overlapping rectangle of bounding rectangles of two MyShape objects
    static MyRectangle overlapMyShapes(MyShape S1, MyShape S2) {
        MyRectangle R1 = S1.getMyBoundingRectangle();
        MyRectangle R2 = S2.getMyBoundingRectangle();
        return overlapMyRectangles(R1, R2);
    }


    //return the set of points of the intersection of two MyShape objects
    static List<MyPoint> intersectMyShapes(MyShape S1, MyShape S2){
        MyRectangle R1 = S1.getMyBoundingRectangle();
        MyRectangle R2 = S2.getMyBoundingRectangle();
        MyRectangle R = overlapMyShapes(R1, R2);

        if (R != null) {
            double x = R.getTLC().getX();
            double y = R.getTLC().getY();
            double w = R.getWidth();
            double h = R.getHeight();

            List <MyPoint> areaIntersect = new ArrayList<>();

            for ( double i = 0; i <= w; i++) {
                double xi = x + i;

                for (double j = 0; j<= h; j++) {
                    MyPoint p = new MyPoint(xi, y + j, null);
                    if (S1.pointInMyShape(p) && S2.pointInMyShape(p)) {
                        areaIntersect.add(p);
                    }
                }
            }
            return areaIntersect;
        } else {
            return null;
        }
    }


    default Canvas drawIntersectMyShapes(double widthCanvas, double heightCanvas, MyShape S1, MyShape S2, MyColor color) {
        List<MyPoint> areaIntersect = intersectMyShapes(S1, S2);

        Canvas overlayCV = new Canvas(widthCanvas, heightCanvas);

        GraphicsContext overlayGC = overlayCV.getGraphicsContext2D();

        S1.getMyBoundingRectangle().stroke(overlayGC);
        S1.draw(overlayGC);
        S2.getMyBoundingRectangle().stroke(overlayGC);
        S2.draw(overlayGC);

        MyRectangle R = overlapMyShapes(S1, S2);
        MyColor colorR = MyColor.FIREBRICK;
        R.setColor(colorR);
        R.stroke(overlayGC);

        if (areaIntersect != null) {
            for (MyPoint p : areaIntersect) {
                p.setColor(color);
                p.draw(overlayGC);
            }
        }

        return overlayCV;
    }
}
