package ync.ysc3232.pictionary_sabotage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import android.text.Layout;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class DrawerView extends View {

    public ViewGroup.LayoutParams params;
    private Path draw_path = new Path();
    private Paint brush = new Paint();

    public DrawerView(Context context) {
        super(context);

        brush.setAntiAlias(true);
        brush.setColor(Color.BLACK);
        brush.setStyle(Paint.Style.STROKE);
        brush.setStrokeJoin(Paint.Join.ROUND);
        brush.setStrokeWidth(8f);

        params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float point_x = event.getX();
        float point_y = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                draw_path.moveTo(point_x, point_y);
                return true;
            case MotionEvent.ACTION_MOVE:
                draw_path.lineTo(point_x, point_y);
                break;
            default:
                return false;

        }
        postInvalidate();
        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(draw_path, brush);
    }
}
