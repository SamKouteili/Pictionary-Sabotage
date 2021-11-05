package ync.ysc3232.pictionary_sabotage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * This view encapsulates all of the drawing functionality of the
 * game, i.e., drawing and erasing.
 *
 * The methods in the class have descriptive names.
 */
public class DrawerView extends View {

    private Path draw_path;
    private Paint brush;
    private Paint canvas_paint;
    private Canvas draw_canvas;
    private Bitmap canvas_bitmap;
    private int paint_color = Color.BLACK;
    private float brush_size;
    private boolean can_draw;

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
        can_draw = false;

        canvas_paint = new Paint(Paint.DITHER_FLAG);
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
        brush.setColor(Color.WHITE);
        brush.setStrokeWidth(15f);
    }
    public void DrawingMode(){
        brush.setColor(paint_color);
        brush.setStrokeWidth(brush_size);
    }

    public void endRound(Context context){


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
                    draw_path.moveTo(touch_x, touch_y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    draw_path.lineTo(touch_x, touch_y);
                    break;
                case MotionEvent.ACTION_UP:
                    draw_path.lineTo(touch_x, touch_y);
                    draw_canvas.drawPath(draw_path, brush);
                    draw_path.reset();
                    break;
                default:
                    return false;
            }
        }
        invalidate();
        return true;
    }
}
