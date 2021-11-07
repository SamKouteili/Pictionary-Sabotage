package ync.ysc3232.pictionary_sabotage;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * PodiumActivity handles the victory screen which appears after
 * the drawing finishes. It shows the top 3 scoring players.
 * @author Jachym
 */
public class PodiumActivity extends AppCompatActivity {

    RoomData roomData;

    //Access rooms database
    DatabaseReference rooms_database = FirebaseDatabase.getInstance("https://pictionary-sabotage-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference().child("Rooms");


    //Default values for the winners
    private final PlayerDbModel sample = new PlayerDbModel("TestPlayer", "Drawer", 42);
    private ArrayList<PlayerDbModel> allPlayers = new ArrayList<>();
    private String roomId;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podium);

        Bundle bundle = getIntent().getExtras();
        roomId = bundle.getString("roomID");


        Button returnToMenuButton = findViewById(R.id.returnToMenuButton);

        //Fetch the corresponding layout text boxes
        TextView fstPos = findViewById(R.id.firstPos);
        TextView sndPos = findViewById(R.id.secondPos);
        TextView trdPos = findViewById(R.id.thirdPos);

        rooms_database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                roomData = snapshot.child(roomId).getValue(RoomData.class);

                for (String id : roomData.players.keySet()) {
                    String role = roomData.players.get(id);
                    int score = roomData.scores.get(id);
                    PlayerDbModel p = new PlayerDbModel(id, role, score);
                    allPlayers.add(p);
                }

                if (allPlayers.isEmpty()) {
                    Log.e("Fetching Podium Data", "Podium Data empty");
                }
                allPlayers.sort(Comparator.comparing(PlayerDbModel::getScore));
                // Set the values to the winners
                String nameAndPoints1 = allPlayers.get(0).getPlayerName() +
                        "\n" + allPlayers.get(0).getScore() + " pts.";
//                String nameAndPoints2 = allPlayers.get(1).getPlayerName() +
//                        "\n" + allPlayers.get(1).getScore() + " pts.";
//                String nameAndPoints3 = allPlayers.get(2).getPlayerName() +
//                        "\n" + allPlayers.get(2).getScore() + " pts.";

                fstPos.setText(nameAndPoints1);
//                sndPos.setText(nameAndPoints2);
//                trdPos.setText(nameAndPoints3);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("firebase", "Error getting existing room.");
            }
        });


        returnToMenuButton.setOnClickListener(view -> {
            Intent intent = new Intent(PodiumActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

}

//    /**
//     * Fetches the players from the database, puts the values into the @allPlayers field.
//     */
//    private void fetchPlayers() {
//        rooms_database.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                roomData = snapshot.child(roomId).getValue(RoomData.class);
//
//                for (String id : roomData.players.keySet()) {
//                    String role = roomData.players.get(id);
//                    int score = roomData.scores.get(id);
//                    PlayerDbModel p = new PlayerDbModel(id, role, score);
//                    allPlayers.add(p);
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e("firebase", "Error getting existing room.");
//            }
//        });
//    }
//
//}