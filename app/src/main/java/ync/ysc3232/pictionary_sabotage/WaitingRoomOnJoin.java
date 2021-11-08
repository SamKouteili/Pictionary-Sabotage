package ync.ysc3232.pictionary_sabotage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

/**
 * When players join room instead of create - they will not be able to
 * choose roles nor startGame
 */

public class WaitingRoomOnJoin extends AppCompatActivity {

    RoomData roomData;
    String roomId;
    //Access rooms database
    DatabaseReference rooms_database = FirebaseDatabase.getInstance("https://pictionary-sabotage-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference().child("Rooms");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room_on_join);

        TextView roomIdText = findViewById(R.id.roomId);
        Button startGame = findViewById(R.id.startGame);
        TextView player1 = findViewById(R.id.player1);
        TextView player2 = findViewById(R.id.player2);
        TextView player3 = findViewById(R.id.player3);
        TextView[] playersText = {player1, player2, player3};

        TextView player1Role = findViewById(R.id.player1Role);
        TextView player2Role = findViewById(R.id.player2Role);
        TextView player3Role = findViewById(R.id.player3Role);
        TextView[] playersRoleText = {player1Role, player2Role, player3Role};

        for (TextView playerRole: playersRoleText) {
            playerRole.setVisibility(View.INVISIBLE);
        }

        //Set room Id
        Bundle bundle = getIntent().getExtras();
        roomId = bundle.getString("roomId");
        roomIdText.setText(roomId);
        Log.d("TAGGG", "Enter Waiting Room with roomId " + roomId);

        //Disbale StartGame Button
        startGame.setEnabled(false);

        //Get room data and players to set the Text on screen
        rooms_database.child(roomId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                roomData = snapshot.getValue(RoomData.class);

                //If the data update says game has started - move to next page
                if (roomData.isGameStarted()) {
                    Log.d("TAGG", "Game Started for " + roomId);
                    Intent intent = new Intent(WaitingRoomOnJoin.this, RandomWordGenerator.class);
                    intent.putExtra("roomID", roomId);
                    startActivity(intent);
                } else {
                    int i = 0;
                    roomData = snapshot.getValue(RoomData.class);
                    for (DataSnapshot playerSnapShot : snapshot.child("players").getChildren()) {
                        Log.d("TAGG", "Players include " + playerSnapShot.getKey());
                        playersText[i].setText(playerSnapShot.getKey());
                        playersRoleText[i].setText(playerSnapShot.getValue().toString());
                        playersRoleText[i].setVisibility(View.VISIBLE);
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
}