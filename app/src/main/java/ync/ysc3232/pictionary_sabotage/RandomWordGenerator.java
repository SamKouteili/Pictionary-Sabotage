package ync.ysc3232.pictionary_sabotage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

public class RandomWordGenerator extends AppCompatActivity {

    Timer timer;

    private TextView countdownText;
    private CountDownTimer countDownTimer;
    private long timeLeftInMilliseconds = 6000;
    private boolean timerRunning;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_word_generator);

        countdownText = findViewById(R.id.countdown_text);
        startTimer();
    }

    public void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMilliseconds, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMilliseconds = l;
                int seconds = (int) timeLeftInMilliseconds / 1000;

                //Update text
                String updatedCountDownTest;
                updatedCountDownTest = "" + seconds;
                countdownText.setText(updatedCountDownTest);

            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(RandomWordGenerator.this, DrawingActivity.class);
                startActivity(intent);
            }
        }.start();
    }
}