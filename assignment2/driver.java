import java.util.*;
import java.io.*;

public class driver {
    public static void main(String[] args) throws FileNotFoundException {
        SoccerTeam bearCat = new SoccerTeam("bearcatlist.txt");
        SoccerTeam kings = new BasketBallTeam("kingslist.txt");
    }

    /* this methods accepts an arraylist of players and displays the info about all the players. 
    Call this method two times using the object kings and bearCat. You must use the same method */
    public static void listPlayers(ArrayList<Player> players) {

    }

    /* a method that accepts an array of SoccerPlayer and lists all the players with fouls. */
    public static void listAllPlayersWithFouls(ArrayList<SoccerPlayer> players) {

    }

    /* a method called searchPlayer that accepts an arraylist of type Player and a name of a player. 
    This method searches the list, if the player is found display the info, otherwise print a message 
    that the player does not exist. */
    public static void searchPlayer(ArrayList<Player> players) {

    }

    /* a method that display the best player with the most goals/points */
    public static void bestPlayerMostGoals() {

    }

    /* a method that takes the team name as parameter and simulates series of 10 games and each game is a
    random play. For each game, you need to generate a random number, if the random number is bigger than 
    0.5.its a win; otherwise the team loses a game. This method prints the number of wins and losses of
     each team. Call this method twice, once by passing in “Bear Cat” as team name and other for passing 
     in “Kings” as the team name from the main method. */
    public static playGames(String teamName) {

    }

    /* a method that removes a player with most red cards(Soccer) / least points(Basketball)and adds a 
    new player in his place. */
    public static removePlayer() {

    }

}