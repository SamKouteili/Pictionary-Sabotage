package ync.ysc3232.pictionary_sabotage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private int num_of_words;


    private String[] words = {"tree", "bench", "squirrel", "hat", "nose"};

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


        //Generate a random word from the string array - get words from data base
        DatabaseReference rw_database = FirebaseDatabase.getInstance("https://pictionary-sabotage-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference().child("random_words");

        rw_database.child("num_words").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting number of words", task.getException());
                }
                else {
//                    Log.d("firebase", "Got number of words " + String.valueOf(task.getResult().getValue()));
                    num_of_words = Integer.parseInt(String.valueOf(task.getResult().getValue()));
                }
            }
        });

        //Generate random number

        int x = (int)(Math.random() * words.length);

        // Read from the database
        rw_database.child(String.valueOf(x)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                randomWord.setText(snapshot.getValue().toString());

                //Only start time when word is generated
                startTimer();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("firebase", "Data not retrieved");
            }
        });

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
                Intent intent = new Intent(RandomWordGenerator.this, SaboteurActivity.class);
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
