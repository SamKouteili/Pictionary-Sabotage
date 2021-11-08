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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    private ArrayList<PlayerDbModel> allPlayers = new ArrayList<>();
    private String roomId;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podium);

        Bundle bundle = getIntent().getExtras();
        roomId = bundle.getString("roomID");

        Button returnButton = findViewById(R.id.returnToMenuButton);
        returnButton.setOnClickListener(view -> {
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

        rooms_database.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                roomData = task.getResult().child(roomId).getValue(RoomData.class);

                String guesserID = "";
                String saboteurID = "";
                String drawerID = "";

                for (String id : roomData.players.keySet()) {
                    if (roomData.players.get(id).equals("Guesser")){
                        guesserID = id;
                    } else if (roomData.players.get(id).equals("Saboteur")){
                        saboteurID = id;
                    } else {
                        drawerID = id;
                    }

                }

                // allPlayers.sort(Comparator.comparing(PlayerDbModel::getScore));
//
//                String name1 = allPlayers.get(0).getPlayerName();
//                String points1 = allPlayers.get(0).getScore() + " pts.";
//                String name2 = allPlayers.get(1).getPlayerName();
//                String points2 = allPlayers.get(1).getScore() + " pts.";
//                String name3 = allPlayers.get(2).getPlayerName();
//                String points3 = allPlayers.get(2).getScore() + " pts.";

                if (roomData.scores.get(saboteurID) > roomData.scores.get(guesserID)) {
                    winner1.setText(saboteurID);
                    ptsw1.setText(roomData.scores.get(saboteurID));
                    winner2.setVisibility(View.INVISIBLE);
                    ptsw2.setVisibility(View.INVISIBLE);
                    crown2.setVisibility(View.INVISIBLE);

                    looser1.setText(guesserID);
                    ptsl1.setText(roomData.scores.get(guesserID));
                    looser2.setText(drawerID);
                    ptsl2.setText(roomData.scores.get(guesserID));
                } else {
                    looser1.setText(saboteurID);
                    ptsl1.setText(roomData.scores.get(saboteurID));
                    looser2.setVisibility(View.INVISIBLE);
                    ptsl2.setVisibility(View.INVISIBLE);
                    skull2.setVisibility(View.INVISIBLE);

                    winner1.setText(guesserID);
                    ptsw1.setText(roomData.scores.get(guesserID));
                    winner2.setText(drawerID);
                    ptsw2.setText(roomData.scores.get(guesserID));
                }
            }
        });

    }
}