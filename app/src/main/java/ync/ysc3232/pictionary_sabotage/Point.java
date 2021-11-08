package ync.ysc3232.pictionary_sabotage;

/**
 * The Point class holds the information for which position on the screen should the Path go through.
 * This includes the x and y coordinates.
 */

public class Point {
    private float x;
    private float y;

    public Point(float x, float y){
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
