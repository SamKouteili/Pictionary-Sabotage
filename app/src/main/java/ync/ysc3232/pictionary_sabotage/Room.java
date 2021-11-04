package ync.ysc3232.pictionary_sabotage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Room extends AppCompatActivity {

    private Button createRoom;
    private Button joinRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        createRoom = findViewById(R.id.createRoom);
        joinRoom = findViewById(R.id.joinRoom);

        //Temporary - createRoom goes to word generator
        createRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Room.this, RandomWordGenerator.class);
                startActivity(intent);
            }
        });
    }
}