package ync.ysc3232.pictionary_sabotage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WaitingRoom extends AppCompatActivity {

    private TextView roomId;
    private TextView player1;
    private TextView player2;
    private TextView player3;
    private TextView player4; //Maximum 4 players
    private Button startGame;

//    String room = getIntent().getStringExtra("roomId");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);
        roomId = findViewById(R.id.roomId);

        Bundle bundle = getIntent().getExtras();
        roomId.setText(bundle.getString("roomId"));

        startGame = findViewById(R.id.startGame);
        startGame.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(WaitingRoom.this, RandomWordGenerator.class);
                startActivity(intent);
            }
        });
    }
}