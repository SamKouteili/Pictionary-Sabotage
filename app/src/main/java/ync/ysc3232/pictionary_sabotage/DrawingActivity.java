package ync.ysc3232.pictionary_sabotage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

/**
 *  This activity connects the Drawer's view to the main game loop.
 *  It allows the player to select different features from the
 *  DrawerView class like drawing or erasing and also connects
 *  to the RandomWordGenerator which handles the game loop.
 */
public class DrawingActivity extends AppCompatActivity {

    String roomID;
    RoomData roomData;
    private int cur_round;
    // Access rooms database
    DatabaseReference room_database = FirebaseDatabase.getInstance("https://pictionary-sabotage-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference().child("Rooms");

    private Toolbar bottom_toolbar;
    private DrawerView drawer_view;

    private long timeLeftToDraw = 20000; // 20 seconds
    private CountDownTimer countDownTimer;
    private TextView countdownText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);


        Log.d("TAGGG", getCurrentUser() + " is in DrawingActivity");

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
                        countDownTimer.cancel();
                    }
                    if (cur_round < 4){
                        Intent intent = new Intent(DrawingActivity.this, RandomWordGenerator.class);
                        intent.putExtra("roomID", roomID);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(DrawingActivity.this, PodiumActivity.class);;
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
                int seconds = (int) (timeLeftToDraw + 1000) / 1000; // Start from 20, end in 1

                //Update text
                String updatedCountDownTest;
                updatedCountDownTest = "" + seconds;
                countdownText.setText(updatedCountDownTest);

            }

            @Override
            public void onFinish() {
                Log.d("TAGGG", getCurrentUser() + " DrawingActivity timer finished");
                // should technically wait for GuesserActivity
//                Intent intent = new Intent(DrawingActivity.this, RandomWordGenerator.class);
//                intent.putExtra("roomID", roomID);
//                startActivity(intent);
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