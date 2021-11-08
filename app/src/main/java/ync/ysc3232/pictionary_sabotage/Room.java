package ync.ysc3232.pictionary_sabotage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Room extends AppCompatActivity {

    private Button createRoom;
    private Button joinRoom;
    private EditText eneteredRoomId;
    private boolean validRoomId;
    private RoomData roomData;
    private String currRoomId;


    DatabaseReference database = FirebaseDatabase.getInstance("https://pictionary-sabotage-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        createRoom = findViewById(R.id.createRoom);
        joinRoom = findViewById(R.id.joinRoom);
        eneteredRoomId = findViewById(R.id.enterRoomId);

        //At createRoom, immediately create a new room with a new code and go to Waiting Room
        createRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create a random room number
                String roomId = String.valueOf((int)(Math.random() * 10000));
                String[] random_words = {"", "", "", "", ""};
                Set<Integer> words_nums = new HashSet<>();
                while (words_nums.size() < 5) {
                    int i = (int)(Math.random() * 10);
                    words_nums.add(Integer.valueOf(i));
                }

                Log.d("Room", "Clicked on CreateRoom with a new roomId " + roomId.toString());

                //Instead of using OnDataChange - we just get once and don't listen to change
                database.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                        else {
                            DataSnapshot snapshot = task.getResult();
                            //Generate new room on database
                            DatabaseReference newRoom = database.child("Rooms").child(roomId);

                            //Generate its random words
                            int j = 0;
                            for (int i : words_nums) {
                                random_words[j] = snapshot.child("random_words").child(String.valueOf(i)).getValue().toString();
                                j += 1;
                            }

                            //Push data of new room
                            roomData = new RoomData(roomId, Arrays.asList(random_words));
                            roomData.addPlayer(getCurrentUser(), "Undecided");
                            newRoom.setValue(roomData);

                            //Passing room Id using intent
                            Log.d("Room", "Intent created to WaitingRoom with roomId " + roomId.toString());
                            Intent intent = new Intent(Room.this, WaitingRoom.class);
                            intent.putExtra("roomId", roomId);
                            startActivity(intent);
                        }
                    }
                });
            }
        });

        joinRoom.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String roomId = eneteredRoomId.getText().toString().trim();

                //Make sure room number is entered
                if (roomId.isEmpty()){
                    eneteredRoomId.setError("Room number is empty");
                    return;
                }

                //Fetch current room data
                database.child("Rooms").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        roomData = snapshot.child(roomId).getValue(RoomData.class);

                        if (roomData == null) {
                            Toast.makeText(Room.this, "Invalid Room Number", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        roomData.addPlayer(getCurrentUser(), "Undecided");
                        database.child("Rooms").child(roomId).setValue(roomData);

                        Intent intent = new Intent(Room.this, WaitingRoomOnJoin.class);
                        intent.putExtra("roomId", roomId);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("firebase", "Error getting existing room.");
                        Toast.makeText(Room.this, "Invalid Room Number", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });

        ImageView img = (ImageView)findViewById(R.id.backg);
        img.setBackgroundResource(R.drawable.bg_animation);
        AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
        frameAnimation.start();
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