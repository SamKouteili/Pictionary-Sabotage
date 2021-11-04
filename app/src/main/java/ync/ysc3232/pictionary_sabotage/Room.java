package ync.ysc3232.pictionary_sabotage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Room extends AppCompatActivity {

    private Button createRoom;
    private Button joinRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        createRoom = findViewById(R.id.createRoom);
        joinRoom = findViewById(R.id.joinRoom);

        DatabaseReference room_database = FirebaseDatabase.getInstance("https://pictionary-sabotage-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference().child("Rooms");



        //At createRoom, immediately create a new room with a new code and go to Waiting Room
        createRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create a random room number
                int roomId = (int)(Math.random() * 10000);

                //TODO: If a room id is taken, we cannot use it again
                //TODO: Create a class for room data
                DatabaseReference newRoom = room_database.child(String.valueOf(roomId));
                Map<String, String> roomData = new HashMap<String, String>();
                roomData.put("no_of_players", "1");
                newRoom.setValue(roomData);

                Intent intent = new Intent(Room.this, RandomWordGenerator.class);
                startActivity(intent);
            }
        });

        ImageView img = (ImageView)findViewById(R.id.backg);
        img.setBackgroundResource(R.drawable.bg_animation);
        AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
        frameAnimation.start();
    }
}