package ync.ysc3232.pictionary_sabotage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

    RoomData roomData;

    //Access rooms database
    DatabaseReference rooms_database = FirebaseDatabase.getInstance("https://pictionary-sabotage-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference().child("Rooms");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);
        TextView roomIdText = findViewById(R.id.roomId);
        Button startGame = findViewById(R.id.startGame);
        TextView player1 = findViewById(R.id.player1);
        TextView player2 = findViewById(R.id.player2);
        TextView player3 = findViewById(R.id.player3);
        TextView player4 = findViewById(R.id.player4);
        TextView[] playersText = {player1, player2, player3, player4};

        Spinner spinner1 = (Spinner) findViewById(R.id.chooseRole1);
        Spinner spinner2 = (Spinner) findViewById(R.id.chooseRole2);
        Spinner spinner3 = (Spinner) findViewById(R.id.chooseRole3);
        Spinner spinner4 = (Spinner) findViewById(R.id.chooseRole4);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.roles, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        Spinner[] spinners = {spinner1, spinner2, spinner3, spinner4};

        //All spinners begin being disabled
        for (Spinner spinner: spinners) {
            spinner.setAdapter(adapter);
            spinner.setEnabled(false);
        }

        //Set room Id
        Bundle bundle = getIntent().getExtras();
        String roomId = bundle.getString("roomId");
        roomIdText.setText(bundle.getString("roomId"));

        //Get room data and players to set the Text and Spinners on screen
        rooms_database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                roomData = snapshot.child(roomId).getValue(RoomData.class);

                //If the data update says game has started - move to next page
                if (roomData.isGameStarted()) {
                    Intent intent = new Intent(WaitingRoom.this, RandomWordGenerator.class);
                    startActivity(intent);
                    return;
                }

                int i = 0;
                roomData = snapshot.child(roomId).getValue(RoomData.class);
                for (DataSnapshot playerSnapShot: snapshot.child(roomId).child("players").getChildren()) {
                    Log.d("waitingRoom", "Players include " + playerSnapShot.getKey());
                    playersText[i].setText(playerSnapShot.getKey());
                    spinners[i].setEnabled(true);
                    ChoosingRoleSpinner spinnerListener = new ChoosingRoleSpinner(roomData, playerSnapShot.getKey());
                    spinners[i].setOnItemSelectedListener(spinnerListener);
                    i += 1;
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
                //Push all roles onto the data base
                roomData.setGameStarted(true);
                rooms_database.child(roomId).setValue(roomData);

                Intent intent = new Intent(WaitingRoom.this, RandomWordGenerator.class);
                intent.putExtra("roomId", roomId);
                startActivity(intent);
            }
        });
    }
}