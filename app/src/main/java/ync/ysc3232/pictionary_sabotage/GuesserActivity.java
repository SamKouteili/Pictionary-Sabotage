package ync.ysc3232.pictionary_sabotage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class GuesserActivity extends AppCompatActivity {

    String roomID;
    RoomData roomData;
    private int cur_round;
    // Access rooms database
    DatabaseReference room_database = FirebaseDatabase.getInstance("https://pictionary-sabotage-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference().child("Rooms");

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

        Log.d("Classes", getCurrentUser() + " is in GuesserActivity");

        input_text = findViewById(R.id.GuesserInput);
        guesser_view = (DrawerView) findViewById(R.id.drawer_view);
        countdownText = findViewById(R.id.countDown_draw);

        // Set room Id
        Bundle bundle = getIntent().getExtras();
        expected_text = bundle.getString("round word");
        roomID = bundle.getString("roomID");
        cur_round = bundle.getInt("round num");

        room_database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                roomData = snapshot.child(roomID).getValue(RoomData.class);

                if (roundEnded()){
                    if (cur_round < 1){
                        Intent intent = new Intent(GuesserActivity.this, RandomWordGenerator.class);
                        intent.putExtra("roomID", roomID);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(GuesserActivity.this, PodiumActivity.class);;
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

        startTimer();

    }

    /**
     * Called when the user submits a word
     * */
    public void sendMessage(View view) {
        String t = input_text.getText().toString().toUpperCase();
        if (t.equals(expected_text.toUpperCase())){
            input_text.setError(null);

            updateGuesserAndDrawerScore();

            // This round complete, begin process to go to next round
            roomData.incrementRoundNum();

            countDownTimer.cancel();

            // Update Database (also acts as ping)
            room_database.child(roomID).setValue(roomData);

        } else { input_text.setError("Wrong Guess!"); }
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

                updateSaboteurScore();

                // Set Game ended to true (necessary for Drawer and Saboteur)
                roomData.incrementRoundNum();

                // Update Database (also acts as ping)
                room_database.child(roomID).setValue(roomData);

            }
        }.start();
    }

    /**
     * If the round number has been incremented, this signals the end of the round
     */
    private boolean roundEnded(){
        return (roomData.getRoundNum() == cur_round + 1);
    }

    /**
     * If we reach the end of the main timer, update the score for the saboteur
     */
    private void updateSaboteurScore(){
        // Round ended and word not guessed; Update score for Saboteur
        for (String id : roomData.players.keySet()) {
            if (roomData.players.get(id).equals("Saboteur")) {
                int count = roomData.scores.containsKey(id) ? roomData.scores.get(id) : 0;
                roomData.scores.put(id, count + 1);
            }
        }
    }

    /**
     * If we reach the end of the main timer, update the score for guesser and drawer
     */
    private void updateGuesserAndDrawerScore(){
        // Update score for Drawer and Guesser
        for (String id : roomData.players.keySet()) {
            if (!roomData.players.get(id).equals("Saboteur")) {
                int count = roomData.scores.containsKey(id) ? roomData.scores.get(id) : 0;
                roomData.scores.put(id, count + 1);
            }
        }
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