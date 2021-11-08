package ync.ysc3232.pictionary_sabotage;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Map;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void playerDbModelScoreTest (){
        PlayerDbModel s = new PlayerDbModel("John", "Guesser", 31);
        assert(s.getScore() == 31);
        s.setScore(41);
        assert(s.getScore() == 41);
    }
    @Test
    public void playerDbModelNameTest (){
        PlayerDbModel s = new PlayerDbModel("John", "Guesser", 31);
        assert(s.getPlayerName().equals("John"));
        s.setPlayerName("Bob");
        assert(s.getPlayerName().equals("Bob"));
    }

    @Test
    public void roomDataRoomIDTest () {
        RoomData room = new RoomData("2402");
        assertEquals(room.getRoomId(), "2402");
        room.setRoomId("Potato");
        assertEquals(room.getRoomId(), "Potato");
    }

    @Test
    public void roomDataScoreTest () {
        RoomData room = new RoomData("2402");
        room.addPlayer("John", "Guesser");
        room.addScoreTo("John");
        room.addScoreTo("John");
        room.addScoreTo("John");
        Map<String, Integer> scores = room.getScores();
        int score = scores.getOrDefault("John", 0);
        assertEquals(score, 3);
    }
    @Test
    public void roomDataSetPlayersTest () {
        RoomData room = new RoomData("2402");
        room.addPlayer("John", "Guesser");
        assertFalse(room.isGameStarted());
        Map<String, String> players = room.getPlayers();
        String role = players.getOrDefault("John", "");
        assert (role.equals("Guesser"));
    }


}