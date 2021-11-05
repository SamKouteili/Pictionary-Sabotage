package ync.ysc3232.pictionary_sabotage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Random;

/**
 *  This activity connects the Drawer's view to the main game loop.
 *  It allows the player to select different features from the
 *  DrawerView class like drawing or erasing and also connects
 *  to the RandomWordGenerator which handles the game loop.
 */
public class DrawingActivity extends AppCompatActivity {

    private Toolbar bottom_toolbar;
    private DrawerView drawer_view;

    private long timeLeftToDraw = 10000; //10 seconds
    private CountDownTimer countDownTimer;
    private TextView countdownText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);

        drawer_view = (DrawerView)findViewById(R.id.drawer_view);
        drawer_view.setCanDraw(true);
        countdownText = findViewById(R.id.countDown_draw);

        bottom_toolbar = (Toolbar) findViewById(R.id.toolbar_bottom);
        bottom_toolbar.inflateMenu(R.menu.menu_drawing);
        bottom_toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                handleDrawingIconTouched(item.getItemId());
                return false;
            }
        });

        startTimer();

    }

    /**
     * Switches between drawing and erasing modes based on the icon
     * clicked.
     *
     * @param itemId - id of the tool pressed (draw / erase )
     */
    private void handleDrawingIconTouched(int itemId){
        switch (itemId){
            case R.id.action_erase:
                drawer_view.EraserMode();
                break;
            case R.id.action_brush:
                drawer_view.DrawingMode();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    /**
     * Starts the main game loop by passing this activity to the RandomWordGenerator
     * updates the countdown timer.
     */
    public void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftToDraw, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftToDraw = l;
                int seconds = (int) (timeLeftToDraw + 1000) / 1000; //Start from 10, end in 1

                //Update text
                String updatedCountDownTest;
                updatedCountDownTest = "" + seconds;
                countdownText.setText(updatedCountDownTest);

            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(DrawingActivity.this, RandomWordGenerator.class);
                startActivity(intent);
            }
        }.start();
    }
}