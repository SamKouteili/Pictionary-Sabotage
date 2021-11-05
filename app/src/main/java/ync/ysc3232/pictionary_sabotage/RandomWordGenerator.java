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
import com.google.firebase.auth.FirebaseAuth;
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
    private String roomId;
    private RoomData roomData;
    private String currRole;

    private String[] words = {"tree", "bench", "squirrel", "hat", "nose"};

    //Generate a random word from the string array - get words from data base
    DatabaseReference database = FirebaseDatabase.getInstance("https://pictionary-sabotage-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference();
    /**
     * Instantiates round instance. Choses a random word from the word bank
     * and runs the timer.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_word_generator);

        //Get the room id
        Bundle bundle = getIntent().getExtras();
        roomId = bundle.getString("roomId");

        countdownText = findViewById(R.id.countdown_text);
        randomWord = findViewById(R.id.randomWord);

        database.child("random_words").child("num_words").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting number of words", task.getException());
                }
                else {
                    num_of_words = Integer.parseInt(String.valueOf(task.getResult().getValue()));
                }
            }
        });

        //Generate random number
        int x = (int)(Math.random() * words.length);

        // Read from the database to generate random word
        database.child("random_words").child(String.valueOf(x)).addValueEventListener(new ValueEventListener() {
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
                String curUsr = getCurrentUser();
                database.child("Rooms").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        roomData = snapshot.child(roomId).getValue(RoomData.class);
                        currRole = roomData.players.get(curUsr);
                        Log.d("RandomWordGenerator", "my role is " + currRole);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("firebase", "Could not get room in RandomWordGenerator");
                    }
                });

                Intent intent = new Intent(RandomWordGenerator.this, GuesserActivity.class);
                intent.putExtra("round word", randomWord.toString().trim());
                intent.putExtra("roomID", roomId);
                startActivity(intent);
            }
        }.start();
    }

    private Intent assign_intent(@NonNull String role) {
        Intent i;
        switch (role){
            case "Guesser" :
                i = new Intent(RandomWordGenerator.this, GuesserActivity.class);
                return i;
            case "Saboteur" :
                i = new Intent(RandomWordGenerator.this, SaboteurActivity.class);
                return i;
            case "Drawer" :
                i = new Intent(RandomWordGenerator.this, DrawingActivity.class);
                return i;
        }
        return null;
    }

    /**
     * Stops the count down timer.
     */
    public void stopTimer() {
        countDownTimer.cancel();
    }

    public String getCurrentUser(){
        //Get current user
        //Remove the email @
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String cUsrEmail = mAuth.getCurrentUser().getEmail();
        int userAt = cUsrEmail.lastIndexOf("@");
        String userId = cUsrEmail.substring(0, userAt);
        return userId;
    }
}
