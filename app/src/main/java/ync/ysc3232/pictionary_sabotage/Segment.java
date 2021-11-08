package ync.ysc3232.pictionary_sabotage;

import java.util.ArrayList;
import java.util.List;

/**
 * Segment stores all the necessary information to re-create a path on a different device.
 * This includes the color and the size of the brush used to draw that path. As well as the
 * list of Points that represent the positions in where each part of the path goes through.
 */

public class Segment {
    private List<Point> points = new ArrayList<Point>();
    private int color;
    private float size;

    public Segment(int color, float stroke_width){
        this.color = color;
        this.size = stroke_width;
    }

    public void addPoints(float x, float y){
        Point p = new Point(x, y);
        points.add(p);
    }

    public List<Point> getPoints() {
        return points;
    }

    public int getColor() {
        return color;
    }

    public float getSize() {
        return size;
    }
}
