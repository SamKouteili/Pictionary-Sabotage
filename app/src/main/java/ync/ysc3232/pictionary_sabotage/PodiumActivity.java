package ync.ysc3232.pictionary_sabotage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * PodiumActivity handles the victory screen which appears after
 * the drawing finishes. It shows the top 3 scoring players.
 * @author Jachym
 */
public class PodiumActivity extends AppCompatActivity {
    private FirebaseAnalytics mFirebaseAnalytics;

    //Default values for the winners
    private String[] winners = {"Drawer", "Saboteur", "Guesser"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podium);

        Button button = findViewById(R.id.returnToMenuButton);

        //Fetch the corresponding layout text boxes
        TextView fstPos = findViewById(R.id.firstPos);
        TextView sndPos = findViewById(R.id.secondPos);
        TextView trdPos = findViewById(R.id.thirdPos);

        // Set the values to the winners
        fstPos.setText(winners[0]);
        sndPos.setText(winners[1]);
        trdPos.setText(winners[2]);


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
    public void setWinners(String fst, String snd, String trd) {
            this.winners = new String[]{fst, snd, trd};
    }
}