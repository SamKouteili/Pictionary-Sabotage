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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/** Waiting Room: After players joined a unique room, they come here
 *  There are maximum 4 players.
 *  Player IDs who joined the room are seen.
 *  Players can choose what role they will be playing as (on the Spinner).
 *  Room has a "Start Game" button that leads all players into the game.
 *
 *  Depending on what the current user's role is, WaitingRoom.java can lead to either
 *  SaboteurActivity.java, DrawingAcitivity.java, or GuesserActivity.java
 */

public class WaitingRoom extends AppCompatActivity {

    RoomData roomData;
    String roomId;
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
        TextView[] playersText = {player1, player2, player3};

        Spinner spinner1 = (Spinner) findViewById(R.id.chooseRole1);
        Spinner spinner2 = (Spinner) findViewById(R.id.chooseRole2);
        Spinner spinner3 = (Spinner) findViewById(R.id.chooseRole3);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.roles, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        Spinner[] spinners = {spinner1, spinner2, spinner3};

        // All spinners begin being disabled
        // TODO: Maybe they should all begin as "Undecided"
        for (Spinner spinner: spinners) {
            spinner.setAdapter(adapter);
            spinner.setEnabled(false);
        }

        //Set room Id
        Bundle bundle = getIntent().getExtras();
        roomId = bundle.getString("roomId");
        roomIdText.setText(roomId);
        Log.d("TAGGG", "Enter Waiting Room with roomId " + roomId);


        startGame.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                // If all players chose unique roles, then update database to start game

                if (playersChoseRoles(roomData.players)){
                    if (allRolesUnique(roomData.players)){
                        startGame.setError(null);
                        roomData.setGameStarted(true);
                        rooms_database.child(roomId).setValue(roomData);
                    } else {
                        Log.d("TAGGG", "Players must chose different roles!");
                        startGame.setError("Players must chose different roles!");
                    }
                } else {
                    Log.d("TAGGG", "All players must chose a role!");
                    startGame.setError("All players must chose a role!");
                }

            }
        });

        //Get room data and players to set the Text and Spinners on screen
        rooms_database.child(roomId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                roomData = snapshot.getValue(RoomData.class);

                //Testing
                Bundle bundle = getIntent().getExtras();
                roomId = bundle.getString("roomId");
                Log.d("TAGGG", "WaitingRoom has bundle with roomId " + roomId);


                //If the data update says game has started - move to next page
                if (roomData.isGameStarted()) {
                    Log.d("TAGGG", "Game Started for " + roomId);
                    Intent intent = new Intent(WaitingRoom.this, RandomWordGenerator.class);
                    intent.putExtra("roomID", roomId);
                    startActivity(intent);
                } else {
                    int i = 0;
                    roomData = snapshot.getValue(RoomData.class);
                    for (DataSnapshot playerSnapShot : snapshot.child("players").getChildren()) {
                        Log.d("TAGGG", "Players include " + playerSnapShot.getKey());
                        playersText[i].setText(playerSnapShot.getKey());
                        spinners[i].setEnabled(true);
                        ChoosingRoleSpinner spinnerListener = new ChoosingRoleSpinner(roomData, playerSnapShot.getKey());
                        spinners[i].setOnItemSelectedListener(spinnerListener);
                        i += 1;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("firebase", "Error in getting players");
            }
        });

    }

    private boolean playersChoseRoles(Map<String, String> player_roles){
        for (Map.Entry<String, String> role : player_roles.entrySet()) {
            // Log.d("Player roles allocated", role.getValue());
            if (role.getValue().equals("Undecided")){
                return false;
            }
        }
        return true;
    }

    private boolean allRolesUnique(Map<String, String> player_roles){
        ArrayList<String> allocated_roles = new ArrayList<String>();
        for (Map.Entry<String, String> role : player_roles.entrySet()) {
            // Log.d("Player roles allocated", role.getValue());
            if (allocated_roles.contains(role.getValue())){
                return false;
            } else {
                allocated_roles.add(role.getValue());
            }
        }
        return true;
    }

}