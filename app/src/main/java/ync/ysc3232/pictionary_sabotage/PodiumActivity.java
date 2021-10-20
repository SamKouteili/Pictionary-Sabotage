package ync.ysc3232.pictionary_sabotage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

public class PodiumActivity extends AppCompatActivity {
    private FirebaseAnalytics mFirebaseAnalytics;

    private String[] winners = {"Drawer", "Saboteur", "Guesser"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podium);

        Button button = findViewById(R.id.returnToMenuButton);
        TextView fstPos = findViewById(R.id.firstPos);
        TextView sndPos = findViewById(R.id.secondPos);
        TextView trdPos = findViewById(R.id.thirdPos);

        fstPos.setText(winners[0]);
        sndPos.setText(winners[1]);
        trdPos.setText(winners[2]);


        button.setOnClickListener(view -> {
            Intent intent = new Intent(PodiumActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    public void setWinners(String fst, String snd, String trd) {
            this.winners = new String[]{fst, snd, trd};
    }
}