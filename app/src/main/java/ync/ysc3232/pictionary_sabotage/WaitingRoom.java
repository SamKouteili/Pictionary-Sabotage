package ync.ysc3232.pictionary_sabotage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WaitingRoom extends AppCompatActivity {

    private TextView roomIdText;
    private TextView player1;
    private TextView player2;
    private TextView player3;
    private TextView player4; //Maximum 4 players
    private Button startGame;
    private Spinner spinner1;

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
        TextView[] playersText = {player1, player2, player3, player4};

        spinner1 = (Spinner) findViewById(R.id.chooseRole1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.roles, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner1.setAdapter(adapter);

        //Set room Id
        Bundle bundle = getIntent().getExtras();
        String roomId = bundle.getString("roomId");
        roomIdText.setText(bundle.getString("roomId"));

        //Access room's information
        DatabaseReference room_database = FirebaseDatabase.getInstance("https://pictionary-sabotage-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference().child("Rooms").child(roomId);

        //Get players
        room_database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 0;
                for (DataSnapshot playerSnapShot: snapshot.child("players").getChildren()) {
                    playersText[i].setText(playerSnapShot.getKey());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("firebase", "Error in getting players");
            }
        });

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