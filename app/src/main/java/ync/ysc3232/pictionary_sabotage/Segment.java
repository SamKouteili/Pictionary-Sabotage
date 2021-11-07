package ync.ysc3232.pictionary_sabotage;

import java.util.ArrayList;
import java.util.List;

public class Segment {
    private List<Point> points = new ArrayList<Point>();
    private int color;
    private float stroke_width;

    public Segment(int color, float stroke_width){
        this.color = color;
        this.stroke_width = stroke_width;
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

    public float getStroke_width() {
        return stroke_width;
    }
}
