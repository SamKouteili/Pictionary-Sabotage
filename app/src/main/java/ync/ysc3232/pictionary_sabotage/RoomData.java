package ync.ysc3232.pictionary_sabotage;

import java.util.List;
import java.util.Map;

public class RoomData {

    String roomId;
    int numberOfPlayers;
    Map<String, String> players;
    Map<String, Integer> scores;

    //Roles will match their ids
    /**
     * 0 - Undecided
     * 1 - Guesser
     * 2 - Saboteur
     * 3 - Drawer
     */
    String[] roles = {"Undecided", "Guesser", "Saboteur", "Drawer"};

    public RoomData(String roomId){
        this.roomId = roomId;
    }

    int getNumberOfPlayers(){
        return numberOfPlayers;
    }

    void addPlayer(String player, int roleId){
        numberOfPlayers += 1;
        players.put(player, roles[roleId]);
        scores.put(player, 0);
    }

    void addScoreTo(String player){
        scores.put(player, scores.get(player) + 1);
    }
}
