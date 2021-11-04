package ync.ysc3232.pictionary_sabotage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * RandomWordGenerator calls from an inbuilt word database and provides methods
 * that return one of these words. This is necessary for the pictionary rounds.
 * Class also provides functionality for starting/ending different rounds.
 */
public class RandomWordGenerator extends AppCompatActivity {

    private TextView countdownText;
    private TextView randomWord;
    private CountDownTimer countDownTimer;
    private long timeLeftInMilliseconds = 5000;
    private Button finish;

    String[] words = {"tree", "bench", "squirrel", "hat", "nose"};


    /**
     * Instantiates round instance. Choses a random word from the word bank
     * and runs the timer.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_word_generator);

        countdownText = findViewById(R.id.countdown_text);
        randomWord = findViewById(R.id.randomWord);

        //Generate a random word from the string array
        int x = (int)(Math.random() * words.length);
        randomWord.setText(words[x]);

        //Immediately start the timer
        startTimer();

        finish = findViewById(R.id.finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopTimer();
                Intent intent = new Intent(RandomWordGenerator.this, PodiumActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * Creates a count down timer and starts it.
     */
    public void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMilliseconds, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMilliseconds = l;
                int seconds = (int) (timeLeftInMilliseconds + 1000) / 1000; //Start from 5, end in 1

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

    /**
     * Stops the count down timer.
     */
    public void stopTimer() {
        countDownTimer.cancel();
    }
}
