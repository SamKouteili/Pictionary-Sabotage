package ync.ysc3232.pictionary_sabotage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.startGameButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DrawingActivity.class);
                startActivity(intent);
            }
        });

        ImageView img = (ImageView)findViewById(R.id.backg);
        img.setBackgroundResource(R.drawable.bg_animation);
        AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
        frameAnimation.start();

    }

}