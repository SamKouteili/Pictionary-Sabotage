package ync.ysc3232.pictionary_sabotage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class GuesserActivity extends AppCompatActivity {

    String expected_text = "hello";
    EditText input_text;
    private GuesserView guesser_view;

    private long timeLeftToDraw = 10000; //10 seconds
    private CountDownTimer countDownTimer;
    private TextView countdownText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guesser);

        input_text = findViewById(R.id.GuesserInput);
        guesser_view = (GuesserView) findViewById(R.id.guesser_view);
        countdownText = findViewById(R.id.countDown_draw);


        startTimer();

    }

    /**
     * Called when the user submits a word
     * */
    public void sendMessage(View view) {
        String t = input_text.getText().toString().trim();
        if (!t.equals(expected_text)){
            input_text.setError("Incorrect Guess Inputted!");
        } else {
            input_text.setError(null);
            // TODO: Add points to database
            // TODO: Ping all screens to move to next RandomWordGenerator session

        }
    }

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
                Intent intent = new Intent(GuesserActivity.this, RandomWordGenerator.class);
                startActivity(intent);
            }
        }.start();
    }

}