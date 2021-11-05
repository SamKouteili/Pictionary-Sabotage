package ync.ysc3232.pictionary_sabotage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SaboteurActivity extends AppCompatActivity {

    private Button saboteur_button;
    private DrawerView saboteur_view;


    private long timeLeftToDraw = 20000; //10 seconds
    private CountDownTimer countDownTimer;
    private TextView countdownText;

    private CountDownTimer sabotageTimer;
    private CountDownTimer waitTimer;
    private long waitTime = 10000;
    private long sabotageTime = 3000;
    private EditText countdownToSabotage;
    private EditText countdownToWait;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saboteur);

        saboteur_view = (DrawerView) findViewById(R.id.saboteur_view);
        countdownText = findViewById(R.id.countDown_draw);

        saboteur_button = (Button) findViewById(R.id.sabotage_button);

        saboteur_button.setBackgroundColor(Color.RED);

//        String w = "10";
//        saboteur_button.setText(w);
//        saboteur_button.setOnClickListener(view -> {
//
//        });

        startTimer();
//        waitTimer();

    }

    public void waitTimer() {
        waitTimer = new CountDownTimer(waitTime, 1000) {
            @Override
            public void onTick(long l) {
                waitTime = l;
                int seconds = (int) (waitTime + 1000) / 1000;

                //Update text
                String updatedCountDown;
                updatedCountDown = "" + seconds;
                countdownToSabotage.setText(updatedCountDown);
                saboteur_button.setText(updatedCountDown);

            }

            @Override
            public void onFinish() {

                saboteur_view.setCanDraw(true);
                saboteur_button.setBackgroundColor(Color.GREEN);
                // TODO: Potentially make this reactive to first touch
                sabotageTimer();
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
                countdownToWait.setText(updatedCountDown);

            }

            @Override
            public void onFinish() {
                saboteur_view.setCanDraw(false);
                saboteur_button.setBackgroundColor(Color.RED);
                // call waitTimer() when sabotageTimer finishes
                waitTimer();
            }
        }.start();
    }


    public void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftToDraw, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftToDraw = l;
                int seconds = (int) (timeLeftToDraw + 1000) / 1000;

                //Update text
                String updatedCountDownText;
                updatedCountDownText = "" + seconds;
                countdownText.setText(updatedCountDownText);

            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(SaboteurActivity.this, RandomWordGenerator.class);
                startActivity(intent);
                // TODO: Update Scores accordingly
            }
        }.start();
    }

}