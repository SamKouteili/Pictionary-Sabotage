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

import java.util.Locale;

public class GuesserActivity extends AppCompatActivity {

    // TODO: Update expected_text with call from database
    String expected_text;
    EditText input_text;
    private DrawerView guesser_view;

    private long timeLeftToDraw = 20000; // 20 seconds
    private CountDownTimer countDownTimer;
    private TextView countdownText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guesser);

        input_text = findViewById(R.id.GuesserInput);
        guesser_view = (DrawerView) findViewById(R.id.drawer_view);
        countdownText = findViewById(R.id.countDown_draw);

        //Set room Id
        Bundle bundle = getIntent().getExtras();
        expected_text = bundle.getString("round word");

        startTimer();

    }

    /**
     * Called when the user submits a word
     * */
    public void sendMessage(View view) {
        String t = input_text.getText().toString().trim().toUpperCase();
        if (!t.equals(expected_text.toUpperCase())){
            input_text.setError("Wrong Guess!");
        } else {
            input_text.setError(null);
            // TODO: Add points to database
            // TODO: Ping all screens to move to next RandomWordGenerator session
            // TODO: Move to next room
        }
    }

    public void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftToDraw, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftToDraw = l;
                int seconds = (int) (timeLeftToDraw + 1000) / 1000;

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