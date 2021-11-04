package ync.ysc3232.pictionary_sabotage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WaitingRoom extends AppCompatActivity {

    private TextView roomIdText;
    private TextView player1;
    private TextView player2;
    private TextView player3;
    private TextView player4; //Maximum 4 players
    private Button startGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);
        roomIdText = findViewById(R.id.roomId);
        startGame = findViewById(R.id.startGame);
        player1 = findViewById(R.id.player1);
        player2 = findViewById(R.id.player2);
        player3 = findViewById(R.id.player3);
        player4 = findViewById(R.id.player4);

        //Set room Id
        Bundle bundle = getIntent().getExtras();
        String roomId = bundle.getString("roomId");
        roomIdText.setText(bundle.getString("roomId"));

        //Access room's information
        DatabaseReference room_database = FirebaseDatabase.getInstance("https://pictionary-sabotage-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference().child("Rooms").child(roomId);

        //Try to get current user
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser cUsr = mAuth.getCurrentUser();
        player1.setText(cUsr.getEmail());

        startGame.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Temporarily delete room
                room_database.removeValue();

                Intent intent = new Intent(WaitingRoom.this, RandomWordGenerator.class);
                startActivity(intent);
            }
        });
    }
}