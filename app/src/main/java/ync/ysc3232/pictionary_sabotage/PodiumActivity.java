package ync.ysc3232.pictionary_sabotage;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

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

        //End game
        rooms_database.child(roomId).child("gameStarted").setValue(false);

        Button returnToMenuButton = findViewById(R.id.returnToMenuButton);
        returnToMenuButton.setOnClickListener(view -> {
            Intent intent = new Intent(PodiumActivity.this, MainActivity.class);
            startActivity(intent);
        });

        TextView title = findViewById(R.id.title);
        TextView winners = findViewById(R.id.winners);
        TextView loosers = findViewById(R.id.loosers);

        TextView winner1 = findViewById(R.id.winner1);
        TextView ptsw1 = findViewById(R.id.ptsw1);
        ImageView crown1 = findViewById(R.id.crown1);
        TextView winner2 = findViewById(R.id.winner2);
        ImageView crown2 = findViewById(R.id.crown2);
        TextView ptsw2 = findViewById(R.id.ptsw2);

        TextView looser1 = findViewById(R.id.looser1);
        TextView ptsl1 = findViewById(R.id.ptsl1);
        ImageView skull1 = findViewById(R.id.skull1);
        TextView looser2 = findViewById(R.id.looser2);
        TextView ptsl2 = findViewById(R.id.ptsl2);
        ImageView skull2 = findViewById(R.id.skull2);

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

                String name1 = allPlayers.get(0).getPlayerName();
                String points1 = allPlayers.get(0).getScore() + " pts.";
                String name2 = allPlayers.get(1).getPlayerName();
                String points2 = allPlayers.get(1).getScore() + " pts.";
                String name3 = allPlayers.get(2).getPlayerName();
                String points3 = allPlayers.get(2).getScore() + " pts.";

                if (!roomData.players.get(name1).equals("Saboteur")) {
                    winner1.setText(name1);
                    ptsw1.setText(points1);
                    winner2.setVisibility(View.INVISIBLE);
                    ptsw2.setVisibility(View.INVISIBLE);
                    crown2.setVisibility(View.INVISIBLE);

                    looser1.setText(name2);
                    ptsl1.setText(points2);
                    looser2.setText(name3);
                    ptsl2.setText(points3);
                } else {
                    looser1.setText(name1);
                    ptsl1.setText(points1);
                    looser2.setVisibility(View.INVISIBLE);
                    ptsl2.setVisibility(View.INVISIBLE);
                    skull2.setVisibility(View.INVISIBLE);

                    winner1.setText(name2);
                    ptsw1.setText(points2);
                    winner2.setText(name3);
                    ptsw2.setText(points3);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("firebase", "Error getting existing room.");
            }
        });

    }




//        Button returnToMenuButton = findViewById(R.id.returnToMenuButton);
//
//        //Fetch the corresponding layout text boxes
//        TextView fstPos = findViewById(R.id.firstPos);
//        TextView sndPos = findViewById(R.id.secondPos);
//        TextView trdPos = findViewById(R.id.thirdPos);
//
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
//                if (allPlayers.isEmpty()) {
//                    Log.e("Fetching Podium Data", "Podium Data empty");
//                }
//                allPlayers.sort(Comparator.comparing(PlayerDbModel::getScore));
//                // Set the values to the winners
//                String nameAndPoints1 = allPlayers.get(0).getPlayerName() +
//                        "\n" + allPlayers.get(0).getScore() + " pts.";
////                String nameAndPoints2 = allPlayers.get(1).getPlayerName() +
////                        "\n" + allPlayers.get(1).getScore() + " pts.";
////                String nameAndPoints3 = allPlayers.get(2).getPlayerName() +
////                        "\n" + allPlayers.get(2).getScore() + " pts.";
//
//                fstPos.setText(nameAndPoints1);
////                sndPos.setText(nameAndPoints2);
////                trdPos.setText(nameAndPoints3);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e("firebase", "Error getting existing room.");
//            }
//        });
//
//
//        returnToMenuButton.setOnClickListener(view -> {
//            Intent intent = new Intent(PodiumActivity.this, MainActivity.class);
//            startActivity(intent);
//        });
//    }

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
