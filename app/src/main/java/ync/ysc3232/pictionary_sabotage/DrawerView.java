package ync.ysc3232.pictionary_sabotage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * This view encapsulates all of the drawing functionality of the
 * game, i.e., drawing and erasing.
 *
 * The methods in the class have descriptive names.
 */
public class DrawerView extends View {

    DatabaseReference drawing_canvas_db;
    DatabaseReference erasing_canvas_db;
    private Path draw_path;
    private Paint brush;
    private Paint canvas_paint;
    private Canvas draw_canvas;
    private Bitmap canvas_bitmap;
    private int paint_color;
    private float brush_size;
    private boolean can_draw;
    private Segment curr_segment;

    private void init(){
        this.setBackgroundColor(Color.WHITE);
        paint_color = Color.BLACK;
        brush_size = 8f;
        draw_path = new Path();
        canvas_paint = new Paint(Paint.DITHER_FLAG);

        brush = new Paint();
        brush.setColor(paint_color);
        brush.setAntiAlias(true);
        brush.setStrokeWidth(brush_size);
        brush.setStyle(Paint.Style.STROKE);
        brush.setStrokeJoin(Paint.Join.ROUND);
        brush.setStrokeCap(Paint.Cap.ROUND);
        can_draw = false;
    }

    public DrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    // You have to run this after you make the room.
    public void setCanvas_db(String room_id){

        drawing_canvas_db = FirebaseDatabase.getInstance("https://pictionary-sabotage-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference().child("Canvases").child(room_id).child("draw_canvas");
        erasing_canvas_db = FirebaseDatabase.getInstance("https://pictionary-sabotage-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference().child("Canvases").child(room_id).child("erase_canvas");


        drawing_canvas_db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("CHILD LISTEN", "added!");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("CHILD LISTEN", "changed! ");
                if(snapshot.exists()){
                    if(previousChildName == null) previousChildName = "";
                    switch(previousChildName){
                        case "color": //changed points
                            Iterator<DataSnapshot> x_list = snapshot.child("x_points").getChildren().iterator();
                            Iterator<DataSnapshot> y_list = snapshot.child("y_points").getChildren().iterator();

                            Segment segment = new Segment(Color.BLACK, 8f);

                            while(x_list.hasNext() && y_list.hasNext()){
                                float x = x_list.next().getValue(Double.class).floatValue();
                                float y = y_list.next().getValue(Double.class).floatValue();
                                segment.addPoints(x, y);
                            }
                            drawSegment(segment, brush);
//                            Log.d("POINTS", "points length: "+segment.getPoints().size());
                            break;
                        case "points": //changed size
                            Log.d("SIZE", "size: "+snapshot.getValue().toString());
                            break;
                        default: //changed color
                            Log.d("COLOR", "color: "+snapshot.getValue().toString());
                            break;
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Log.d("CHILD LISTEN", "removed!");

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        erasing_canvas_db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("CHILD LISTEN", "added!");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("CHILD LISTEN", "changed! ");
                if(snapshot.exists()){
                    if(previousChildName == null) previousChildName = "";
                    switch(previousChildName){
                        case "color": //changed points
                            Iterator<DataSnapshot> x_list = snapshot.child("x_points").getChildren().iterator();
                            Iterator<DataSnapshot> y_list = snapshot.child("y_points").getChildren().iterator();

                            Segment segment = new Segment(Color.WHITE, 40f);

                            while(x_list.hasNext() && y_list.hasNext()){
                                float x = x_list.next().getValue(Double.class).floatValue();
                                float y = y_list.next().getValue(Double.class).floatValue();
                                segment.addPoints(x, y);
                            }
                            drawSegment(segment, brush);
//                            Log.d("POINTS", "points length: "+segment.getPoints().size());
                            break;
                        case "points": //changed size
                            Log.d("SIZE", "size: "+snapshot.getValue().toString());
                            break;
                        default: //changed color
                            Log.d("COLOR", "color: "+snapshot.getValue().toString());
                            break;
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Log.d("CHILD LISTEN", "removed!");

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setCanDraw(boolean can_draw){
        this.can_draw = can_draw;
    }

    /**
     * Drawing and Erasing Mode functions for DrawingActivity
     */
    public void eraserMode(){
        paint_color = Color.WHITE;
        brush.setColor(paint_color);
        brush_size = 40f;
        brush.setStrokeWidth(brush_size);

    }
    public void drawingMode(){
        paint_color = Color.BLACK;
        brush.setColor(paint_color);
        brush_size = 8f;
        brush.setStrokeWidth(brush_size);
    }

    public void clearCanvas(){
        canvas_bitmap = Bitmap.createBitmap(canvas_bitmap.getWidth(), canvas_bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        draw_canvas = new Canvas(canvas_bitmap);
        invalidate();
    }

    /**
     * Converts a list of Points into a path that can be drawn onto the canvas
     * @param points
     * @return Path
     */
    public static Path getPathFromPoints(List<Point> points){
        Path path = new Path();

        if(points.isEmpty()) return path;

        Point start = points.get(0);
        path.moveTo(start.getX(), start.getY());
        Point curr = null;
        for (int i = 1; i < points.size(); ++i) {
            curr = points.get(i);
            path.lineTo(curr.getX(), curr.getY());
        }
        return path;
    }

    /**
     * Takes a given segment and draws it onto the canvas.
     * @param segment
     * @param brush
     */
    private void drawSegment(Segment segment, Paint brush){
        brush.setColor(segment.getColor());
        brush.setStrokeWidth(segment.getSize());
        draw_canvas.drawPath(getPathFromPoints(segment.getPoints()), brush);
        invalidate();
    }

    private void pushSegment(Segment segment){
        DatabaseReference canvas_db = erasing_canvas_db;
        if(paint_color == Color.BLACK) canvas_db = drawing_canvas_db;

        canvas_db.child("color").setValue(segment.getColor());
        canvas_db.child("size").setValue(segment.getSize());
        List<Point> points = segment.getPoints();
        DatabaseReference points_ref = canvas_db.child("points");

        DatabaseReference x_points_ref = points_ref.child("x_points");
        DatabaseReference y_points_ref = points_ref.child("y_points");
        points_ref.removeValue();

        for(Point p : points){
            String x_key = x_points_ref.push().getKey();
            String y_key = y_points_ref.push().getKey();
            x_points_ref.child(x_key).setValue(p.getX());
            y_points_ref.child(y_key).setValue(p.getY());
        }
    }

    public void onTouchStart(float x, float y){
        draw_path.reset();
        draw_path.moveTo(x, y);
        curr_segment = new Segment(paint_color, brush_size);
        curr_segment.addPoints(x, y);
        Log.d("TouchEvent", "touch started");
    }

    public void onTouchMove(float x, float y){
        draw_path.lineTo(x, y);
        curr_segment.addPoints(x, y);
        Log.d("TouchEvent", "touch moved");
    }

    public void onTouchStop(float x, float y){
        draw_path.lineTo(x, y);
        draw_canvas.drawPath(draw_path, brush);
        draw_path.reset();
        curr_segment.addPoints(x, y);
        pushSegment(curr_segment);

        Log.d("TouchEvent", "touch stopped");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvas_bitmap, 0 , 0, canvas_paint);
        canvas.drawPath(draw_path, brush);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvas_bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        draw_canvas = new Canvas(canvas_bitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (can_draw){
            float touch_x = event.getX();
            float touch_y = event.getY();

            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    onTouchStart(touch_x, touch_y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    onTouchMove(touch_x, touch_y);
                    break;
                case MotionEvent.ACTION_UP:
                    onTouchStop(touch_x, touch_y);
                    break;
                default:
                    return false;
            }
        }
        invalidate();
        return true;
    }
}