package ync.ysc3232.pictionary_sabotage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomData {

    String roomId;
    int numberOfPlayers;
    Map<String, String> players;
    Map<String, Integer> scores;
    /**
     * Role ids
     * 0 - Undecided
     * 1 - Guesser
     * 2 - Saboteur
     * 3 - Drawer
     */
    List<String> roles;

    public RoomData(){}

    public RoomData(String roomId){
        this.roomId = roomId;
        this.numberOfPlayers = 0;
        this.players = new HashMap<>();
        this.scores = new HashMap<>();
        String[] roles = {"Undecided", "Guesser", "Saboteur", "Drawer"};
        this.roles = Arrays.asList(roles);
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

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    void addPlayer(String player, int roleId){
        if (!players.containsKey(player)) {
            numberOfPlayers += 1;
            players.put(player, roles.get(roleId));
            scores.put(player, 0);
        }
    }

    void changePLayerRole(String player, int roleId){
        players.put(player, roles.get(roleId));
    }

    void addScoreTo(String player){
        scores.put(player, scores.get(player) + 1);
    }
}
