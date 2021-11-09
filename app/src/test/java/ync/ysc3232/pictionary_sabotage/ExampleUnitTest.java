package ync.ysc3232.pictionary_sabotage;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
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

    List<String> fiveWords = Arrays.asList("table", "mountain", "hat", "moose", "cat");

    @Test
    public void roomDataStartGame () {
        RoomData room = new RoomData("2402", fiveWords);
        room.addPlayer("John", "Guesser");
        assertFalse(room.isGameStarted());
        room.setGameStarted(true);
        assert(room.isGameStarted());
        assertEquals(room.getFiveWords(), fiveWords);
        room.setGameStarted(false);
    }

    @Test
    public void roomDataRound () {
        RoomData room = new RoomData("2402", fiveWords);
        assertEquals(room.getRoundNum(), 0);
        room.incrementRoundNum();
        assertEquals(room.getRoundNum(), 1);
    }

    @Test
    public void roomDataRoomIDTest () {
        RoomData room = new RoomData("2402", fiveWords);
        assertEquals(room.getRoomId(), "2402");
        room.setRoomId("Potato");
        assertEquals(room.getRoomId(), "Potato");
    }

    @Test
    public void roomDataScoreTest () {
        RoomData room = new RoomData("2402", fiveWords);
        room.addPlayer("John", "Guesser");
        room.addScoreTo("John");
        room.addScoreTo("John");
        room.addScoreTo("John");
        Map<String, Integer> scores = room.getScores();
        int score = scores.getOrDefault("John", 0);
        assertEquals(score, 3);
    }

    @Test
    public void roomDataSetChangePlayersTest () {
        RoomData room = new RoomData("2402", fiveWords);
        room.addPlayer("John", "Guesser");
        assertFalse(room.isGameStarted());
        Map<String, String> players = room.getPlayers();
        String role = players.getOrDefault("John", "");
        assert (role.equals("Guesser"));
        room.changePLayerRole("John", "Saboteur");
        players = room.getPlayers();
        role = players.getOrDefault("John", "");
        assert (role.equals("Saboteur"));
    }


}