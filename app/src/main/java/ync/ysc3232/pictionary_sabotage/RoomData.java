package ync.ysc3232.pictionary_sabotage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomData {

    String roomId;
    int numberOfPlayers;
    Map<String, String> players; //Map from player to their role
    Map<String, Integer> scores;
    boolean gameStarted;
    boolean gameCompleted;

    public RoomData(){}

    public RoomData(String roomId){
        this.roomId = roomId;
        this.numberOfPlayers = 0;
        this.players = new HashMap<>();
        this.scores = new HashMap<>();
        this.gameStarted = false;
        this.gameCompleted = false;
//        String[] roles = {"Undecided", "Guesser", "Saboteur", "Drawer"};
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public Map<String, String> getPlayers() {
        return players;
    }

    public void setPlayers(Map<String, String> players) {
        this.players = players;
    }

    public Map<String, Integer> getScores() {
        return scores;
    }

    public void setScores(Map<String, Integer> scores) {
        this.scores = scores;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public boolean isGameCompleted() {
        return gameCompleted;
    }

    public void setGameCompleted(boolean gameCompleted) {
        this.gameCompleted = gameCompleted;
    }

    //Do not delete these
    void addPlayer(String player, String roleId){
        if (!players.containsKey(player)) {
            numberOfPlayers += 1;
            players.put(player, roleId);
            scores.put(player, 0);
        }
    }

    void changePLayerRole(String player, String roleId){
        players.put(player, roleId);
    }

    void addScoreTo(String player){
        scores.put(player, scores.get(player) + 1);
    }
}
