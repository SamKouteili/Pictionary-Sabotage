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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This view encapsulates all of the drawing functionality of the
 * game, i.e., drawing and erasing.
 *
 * The methods in the class have descriptive names.
 */
public class DrawerView extends View {

    DatabaseReference canvas_db = FirebaseDatabase.getInstance("https://pictionary-sabotage-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference().child("Canvas");

    private Path draw_path;
    private Paint brush;
    private Paint canvas_paint;
    private Canvas draw_canvas;
    private Bitmap canvas_bitmap;
    private int paint_color = Color.BLACK;
    private float brush_size;
    private boolean can_draw;
    private Set<String> outstanding_segments;
    private Segment curr_segment;

    private void init(){
        brush_size = 8f;
        draw_path = new Path();
        brush = new Paint();
        brush.setColor(paint_color);
        brush.setAntiAlias(true);
        brush.setStrokeWidth(brush_size);
        brush.setStyle(Paint.Style.STROKE);
        brush.setStrokeJoin(Paint.Join.ROUND);
        brush.setStrokeCap(Paint.Cap.ROUND);
        can_draw = true;
        this.setBackgroundColor(Color.WHITE);
        canvas_paint = new Paint(Paint.DITHER_FLAG);
        outstanding_segments = new HashSet<String>();
    }

    public DrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public void setCanDraw(boolean b){
        can_draw = b;
    }

    /**
     * Drawing and Erasing Mode functions for DrawingActivity
     */
    public void EraserMode(){
        paint_color = Color.GREEN;
        brush.setColor(paint_color);
        brush_size = 25f;
        brush.setStrokeWidth(brush_size);
    }
    public void DrawingMode(){
        paint_color = Color.BLACK;
        brush.setColor(paint_color);
        brush_size = 8f;
        brush.setStrokeWidth(brush_size);
    }

    public void ClearCanvas(){
        canvas_bitmap = Bitmap.createBitmap(canvas_bitmap.getWidth(), canvas_bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        draw_canvas = new Canvas(canvas_bitmap);
        invalidate();
    }

    public static Path getPathFromPoints(List<Point> points){
        Path path = new Path();
        Point start = points.get(0);
        path.moveTo(start.getX(), start.getY());
        Point curr = null;
        for (int i = 1; i < points.size(); ++i) {
            curr = points.get(i);
            path.lineTo(curr.getX(), curr.getY());
        }
        return path;
    }

    public void onTouchStart(float x, float y){
        draw_path.reset();
        draw_path.moveTo(x, y);
        curr_segment = new Segment(paint_color, brush_size);
        curr_segment.addPoints(x, y);
        Log.d("function", "here");
    }

    public void onTouchMove(float x, float y){
        draw_path.lineTo(x, y);
        curr_segment.addPoints(x, y);
    }

    public void onTouchStop(float x, float y){
        draw_path.lineTo(x, y);
        draw_canvas.drawPath(draw_path, brush);
        draw_path.reset();
        ClearCanvas();
        draw_canvas.drawPath(getPathFromPoints(curr_segment.getPoints()), brush);
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