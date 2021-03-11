import java.util.*;
import java.io.*;

public class driver {
    public static void main(String[] args) throws FileNotFoundException {
        SoccerTeam bearCat = new SoccerTeam("bearcatlist.txt");
        BasketBallTeam kings = new BasketBallTeam("kingslist.txt");
        println("Step 1");
        // listPlayers(kings.getTeam());
        // listPlayers(bearCat.getTeam());
        println("Step 2");
        ArrayList<SoccerPlayer> soccerPlayers = new ArrayList<SoccerPlayer>();
        for (int i = 0; i< bearCat.getTeam().size(); i++) {
            SoccerPlayer player = (SoccerPlayer)bearCat.getTeam().get(i);
            soccerPlayers.add(player);
        }
        // listAllPlayersWithFouls(soccerPlayers);
        println("Step 3");
        // mostRebound(kings);
        println("Step 4");
        searchPlayer(kings.getTeam(), "Garret Temple");
        searchPlayer(bearCat.getTeam(), "Julian Hernandez");
        println("Step 5");
        bestPlayerMostGoals(kings);
        bestPlayerMostGoals(bearCat);
        println("Step 6");
        playGames("Bear Cat");
        playGames("Kings");
        println("Step 7");
        kings = removePlayer(kings);
        bearCat = removePlayer(bearCat);
    }

    /* this methods accepts an arraylist of players and displays the info about all the players. 
    Call this method two times using the object kings and bearCat. You must use the same method */
    public static void listPlayers(ArrayList<Player> players) {
        for (int i=0; i<players.size(); i++) {
            print(players.get(i).toString());
        }
    }

    /* a method that accepts an array of SoccerPlayer and lists all the players with fouls. */
    public static void listAllPlayersWithFouls(ArrayList<SoccerPlayer> players) {
        for (int i = 0; i< players.size(); i++) {
            if (players.get(i).getFouls() != 0) {
                println(players.get(i).toString());
            }
        }
    }

    /* a method called mostRebound that displays the player with the most rebound */
    public static void mostRebound(BasketBallTeam team) {
        println(team.selectMostRebounds().toString());
    }

    /* a method called searchPlayer that accepts an arraylist of type Player and a name of a player. 
    This method searches the list, if the player is found display the info, otherwise print a message 
    that the player does not exist. It returns the integer of that player or -1 if not found.*/
    public static int searchPlayer(ArrayList<Player> players, String name) {
        for (int i = 0; i< players.size(); i++) {
            if (players.get(i).getName().equals(name)) {
                println(players.get(i).toString());
                return i;
            }
        }
        print("Player does not exist.");
        return -1;
    }

    /* a method that display the best player with the most goals*/
    public static void bestPlayerMostGoals(SoccerTeam team) {
        println(team.selectTheBest().toString());
    }

    /* a method that display the best player with the most points */
    public static void bestPlayerMostGoals(BasketBallTeam team) {
        println(team.selectTheBest().toString());
    }

    /* a method that takes the team name as parameter and simulates series of 10 games and each game is a
    random play. For each game, you need to generate a random number, if the random number is bigger than 
    0.5.its a win; otherwise the team loses a game. This method prints the number of wins and losses of
     each team. Call this method twice, once by passing in “Bear Cat” as team name and other for passing 
     in “Kings” as the team name from the main method. */
    public static void playGames(String teamName) {
        Random rand = new Random();
        int games = 10;
        int wins = 0;
        int losses = 0;
        for (int i=0; i<games; i++) {
            if (rand.nextFloat() > 0.5) {
                wins++;
            } else {
                losses++;
            }
        }
        println(teamName + " won " + wins + " times and lost " + losses + " times");
    }

    /* a method that removes a player with most red cards(Soccer) / least points(Basketball)and adds a 
    new player in his place. */
    public static BasketBallTeam removePlayer(BasketBallTeam team) {
        int worstPlayerIndex = (searchPlayer(team.getTeam(),team.selectTheWorst().getName()));
        team.remove(worstPlayerIndex);
        BasketBallPlayer newGuy = new BasketBallPlayer("Bill Clinton", 107000.00, 
        19, 3, 5, 6, 1, 7);
        team.add(newGuy, worstPlayerIndex);
        return team;
    }

    public static SoccerTeam removePlayer(SoccerTeam team) {
        int worstPlayerIndex = (searchPlayer(team.getTeam(),team.selectMostRed().getName()));
        team.remove(worstPlayerIndex);
        SoccerPlayer newGuy = new SoccerPlayer("Geoffnet die Tur", 50000.00, 
        91, 4, 8, 1, 4, 5, 4);
        team.add(newGuy, worstPlayerIndex);
        return team;
    }

    public static void print(String str) {
        System.out.println(str);
    }

    public static void println(String str) {
        System.out.println(str+"\n");
    }
}