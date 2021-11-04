package ync.ysc3232.pictionary_sabotage;

import android.view.View;
import android.widget.AdapterView;

public class ChoosingRoleSpinner implements AdapterView.OnItemSelectedListener {

    RoomData roomData;
    String playerId;

    public ChoosingRoleSpinner(RoomData roomData, String playerId){
        this.roomData = roomData;
        this.playerId = playerId;
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        String item = parent.getItemAtPosition(pos).toString();
        roomData.changePLayerRole(playerId, item);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }
}
