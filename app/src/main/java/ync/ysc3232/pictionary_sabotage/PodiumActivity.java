package ync.ysc3232.pictionary_sabotage;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Collections;
import java.util.Comparator;

/**
 * PodiumActivity handles the victory screen which appears after
 * the drawing finishes. It shows the top 3 scoring players.
 * @author Jachym
 */
public class PodiumActivity extends AppCompatActivity {
    private FirebaseAnalytics mFirebaseAnalytics;

    //Default values for the winners
    private String[] winners = {"Drawer", "Saboteur", "Guesser"};
    private int[] points = {0,0,0};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podium);

        Button button = findViewById(R.id.returnToMenuButton);

        //Fetch the corresponding layout text boxes
        TextView fstPos = findViewById(R.id.firstPos);
        TextView sndPos = findViewById(R.id.secondPos);
        TextView trdPos = findViewById(R.id.thirdPos);
//      updatePoints(true, allPlayers);
        String s1 = winners[0] + "\n" + points[0];
        String s2 = winners[1] + "\n" + points[1];
        String s3 = winners[2] + "\n" + points[2];

        // Set the values to the winners
        fstPos.setText(s1);
        sndPos.setText(s2);
        trdPos.setText(s3);


        button.setOnClickListener(view -> {
            Intent intent = new Intent(PodiumActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Setter function - fetches the top three scoring players from the database
     * and sets their corresponding place in the winners array.
     * @param fst - top scoring player's name
     * @param snd - second scoring player's name
     * @param trd - thrid scoring player's name
     */
    private void setWinners(String fst, String snd, String trd) {
            this.winners = new String[]{fst, snd, trd};
    }
    private void setPoints(int fst, int snd, int trd){
        this.points = new int[]{fst, snd, trd};
    }

//    /**
//     * This function updates the number of points each player has based on
//     * the outcome of the round.
//     * @param drawerWon - outcome of the round, true if the prompt was guessed correctly
//     * @param allPlayers - array of all players in the game
//     */
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    private void updatePoints(Boolean drawerWon, Player[] allPlayers) {
//        for (Player player : allPlayers) {
//            if (drawerWon && (player == Drawer || player == Guesser)){
//                player.points += 1;
//            } else {
//                if (!drawerWon && player == Saboteur){
//                    player.points += 1;
//                }
//            }
//        }
//        Collections.sort(allPlayers, Comparator.comparingInt(Player::points));
//        setWinners(allPlayers[0].name, allPlayers[1].name, allPlayers[2].name);
//        setPoints(allPlayers[0].points, allPlayers[1].points, allPlayers[2].points);
//    }
}