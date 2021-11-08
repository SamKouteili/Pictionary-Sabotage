package ync.ysc3232.pictionary_sabotage;

import android.view.View;
import android.widget.AdapterView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChoosingRoleSpinner implements AdapterView.OnItemSelectedListener {

    RoomData roomData;
    String playerId;

    DatabaseReference rooms_database = FirebaseDatabase.getInstance("https://pictionary-sabotage-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference().child("Rooms");

    public ChoosingRoleSpinner(RoomData roomData, String playerId){
        this.roomData = roomData;
        this.playerId = playerId;
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        String item = parent.getItemAtPosition(pos).toString();
        roomData.changePLayerRole(playerId, item);
        rooms_database.child(roomData.getRoomId()).setValue(roomData);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }
}
