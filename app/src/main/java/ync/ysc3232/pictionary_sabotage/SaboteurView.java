package ync.ysc3232.pictionary_sabotage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class SaboteurView extends View {

    private Path draw_path;
    private Paint brush;
    private Paint canvas_paint;
    private Canvas draw_canvas;
    private Bitmap canvas_bitmap;

    private boolean can_sabotage;
    private CountDownTimer countDownTimer;
    private CountDownTimer sabotageTimer;
    private long waitTime = 10000;
    private long sabotageTime = 3000;
    private TextView countdownToSabotage;
    private TextView sabotagePeriod;

    private int click_counter;


    private void init(){

        draw_path = new Path();
        brush = new Paint();
        brush.setColor(Color.WHITE);
        brush.setAntiAlias(true);
        brush.setStrokeWidth(20f);
        brush.setStyle(Paint.Style.STROKE);
        brush.setStrokeJoin(Paint.Join.ROUND);
        brush.setStrokeCap(Paint.Cap.ROUND);
        click_counter = 0;

        canvas_paint = new Paint(Paint.DITHER_FLAG);

        can_sabotage = false;
    }

    public SaboteurView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void waitTimer() {
        countDownTimer = new CountDownTimer(waitTime, 1000) {
            @Override
            public void onTick(long l) {
                waitTime = l;
                int seconds = (int) (waitTime + 1000) / 1000;

                //Update text
                String updatedCountDown;
                updatedCountDown = "" + seconds;
                countdownToSabotage.setText(updatedCountDown);

            }

            @Override
            public void onFinish() {
                can_sabotage = true;
            }
        }.start();
    }

    public void sabotageTimer() {
        sabotageTimer = new CountDownTimer(sabotageTime, 1000) {
            @Override
            public void onTick(long l) {
                sabotageTime = l;
                int seconds = (int) (sabotageTime + 1000) / 1000;

                //Update text
                String updatedCountDown;
                updatedCountDown = "" + seconds;
                sabotagePeriod.setText(updatedCountDown);

            }

            @Override
            public void onFinish() {
                can_sabotage = false;
                // call waitTimer() when sabotageTimer finishes
                waitTimer();
                // reset click_counter so that first move made next click
                // activates the sabotageTimer()
                click_counter = 0;
            }
        }.start();
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
        if (can_sabotage){
            float touch_x = event.getX();
            float touch_y = event.getY();

            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    draw_path.moveTo(touch_x, touch_y);
                    // first click down activates sabotageTimer
                    if (click_counter == 0) { sabotageTimer(); }
                    click_counter++;
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
        }

        return true;
    }

}
