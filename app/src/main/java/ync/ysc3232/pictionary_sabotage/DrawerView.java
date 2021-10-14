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

public class DrawerView extends View {

    // this is the path where the drawing vertices are saved
    private Path draw_path;
    // this is the brush that will paint the drawpath
    private Paint brush;
    // i was told to write this idk wat it does
    private Paint canvas_paint;
    // this is the canvas where the drawpaths and brushes are saved
    private Canvas draw_canvas;
    // this is the canvas bitmap
    private Bitmap canvas_bitmap;

    // this is the default paint color
    private int paint_color = Color.BLACK;

    // this is the brush size
    private float brush_size;

    private void init(){
        brush_size = 8f;

        draw_path = new Path();
        brush = new Paint();
        // setting brush attributes
        brush.setColor(paint_color);
        brush.setAntiAlias(true);
        brush.setStrokeWidth(brush_size);
        brush.setStyle(Paint.Style.STROKE);
        brush.setStrokeJoin(Paint.Join.ROUND);
        brush.setStrokeCap(Paint.Cap.ROUND);

        canvas_paint = new Paint(Paint.DITHER_FLAG);
    }

    // initializing everything
    public DrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    // eraser mode is just big white brush
    public void EraserMode(){
        brush.setColor(getResources().getColor(R.color.backg));
        brush.setStrokeWidth(30f);
    }

    // drawing mode is just normal brush
    public void DrawingMode(){
        brush.setColor(paint_color);
        brush.setStrokeWidth(brush_size);
    }

    // when the view is invalidated this method is called, so this is how show the users
    // the drawings
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvas_bitmap, 0 , 0, canvas_paint);
        canvas.drawPath(draw_path, brush);
    }

    // this is creating the canvas when our screen is initialized
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvas_bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        draw_canvas = new Canvas(canvas_bitmap);
    }

    //drawing events
    @Override
    public boolean onTouchEvent(MotionEvent event) {
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
        invalidate();
        return true;
    }
}
