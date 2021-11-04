package ync.ysc3232.pictionary_sabotage;

/**
 * This class is an abstraction of the representation of players in
 * the database. Contains only basic getters and setters.
 */
public class PlayerDbModel {

    private String playerName;
    private String role;
    private int score;

    public PlayerDbModel(String playerName, String role, int score) {
        this.playerName = playerName;
        this.role = role;
        this.score = score;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


}
