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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class SaboteurActivity extends AppCompatActivity {

    String roomID;
    RoomData roomData;
    private int cur_round;
    // Access rooms database
    DatabaseReference room_database = FirebaseDatabase.getInstance("https://pictionary-sabotage-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference().child("Rooms");

    private Button saboteur_button;
    private DrawerView saboteur_view;


    private long timeLeftToDraw = 20000; // 20 seconds
    private CountDownTimer countDownTimer;
    private TextView countdownText;

    private CountDownTimer sabotageTimer;
    private CountDownTimer waitTimer;
    private final long waitTime = 5000;
    private final long sabotageTime = 3000;
    private boolean can_sabotage;
    private boolean has_sabotaged;
    private final String go = "GO!";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saboteur);

        Log.d("TAGGG", getCurrentUser() + " is in SaboteurActivity");

        saboteur_view = (DrawerView) findViewById(R.id.saboteur_view);
        saboteur_view.EraserMode();
        countdownText = findViewById(R.id.countDown_draw);
        can_sabotage = false;
        has_sabotaged = false;
        saboteur_button = (Button) findViewById(R.id.sabotage_button);

        // Set room Id
        Bundle bundle = getIntent().getExtras();
        roomID = bundle.getString("roomID");
        cur_round = bundle.getInt("round num");

        room_database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                roomData = snapshot.child(roomID).getValue(RoomData.class);

                // Should always be true when data updated - just double checking
                if (roundEnded()){
                    if (countDownTimer != null){
                        Log.d("TAGGG", "CountDownTimer cancel");
                        countDownTimer.cancel();
                    }
                    if (cur_round < 3){
                        Intent intent = new Intent(SaboteurActivity.this, RandomWordGenerator.class);
                        intent.putExtra("roomID", roomID);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SaboteurActivity.this, PodiumActivity.class);;
                        intent.putExtra("roomID", roomID);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("firebase", "Error getting existing room.");
            }
        });


        saboteur_button.setOnClickListener(view -> {

            // We only want this to happen once
            if (can_sabotage && !has_sabotaged){
                saboteur_view.setCanDraw(true);
                sabotageTimer();
                has_sabotaged = true;
            }

        });

        startTimer();
        waitTimer();

    }

    public void waitTimer() {
        waitTimer = new CountDownTimer(waitTime, 1000) {
            @Override
            public void onTick(long l) {
                int seconds = (int) (l + 1000) / 1000;

                //Update text
                String updatedCountDown;
                updatedCountDown = "" + seconds;
                saboteur_button.setText(updatedCountDown);

            }

            @Override
            public void onFinish() {
                can_sabotage = true;
                has_sabotaged = false;
                saboteur_button.setBackgroundResource(R.drawable.green_button);
                saboteur_button.setText(go);
            }
        }.start();
    }

    public void sabotageTimer() {
        sabotageTimer = new CountDownTimer(sabotageTime, 1000) {
            @Override
            public void onTick(long l) {
                int seconds = (int) (l + 1000) / 1000;

                //Update text
                String updatedCountDown;
                updatedCountDown = "" + seconds;
                saboteur_button.setText(updatedCountDown);

            }

            @Override
            public void onFinish() {
                saboteur_view.setCanDraw(false);
                can_sabotage = false;
                saboteur_button.setBackgroundResource(R.drawable.red_button);
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
                if (sabotageTimer != null){
                    sabotageTimer.cancel();
                }
                if (waitTimer != null){
                    waitTimer.cancel();
                }
                Log.d("TAGGG", "startTimer Finish in Saboteur");
            }
        }.start();
    }

    private boolean roundEnded(){
        return (roomData.getRoundNum() == cur_round + 1);
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