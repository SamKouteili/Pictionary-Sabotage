package ync.ysc3232.pictionary_sabotage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Random;

/**
 * Default page of the application. Allows to start game or join an existing one.
 */
public class MainActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Button button = findViewById(R.id.startGameButton);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Room.class);
            startActivity(intent);
        });

        ImageView img = (ImageView)findViewById(R.id.backg);
        img.setBackgroundResource(R.drawable.bg_animation);
        AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
        frameAnimation.start();

    }

}