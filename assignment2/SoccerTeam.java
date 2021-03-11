import java.util.*;
import java.io.*;

public class SoccerTeam extends Team {
    // private ArrayLisst<SoccerPlayers> soccerTeam;
    /* +constructor()	Reads the info for the players from a file and creates an object of 
    SoccerPlayer and then stores it in the  arrayList called team. Then call super constructor 
    and pass team name and array list.*/
    public SoccerTeam(String fileName) throws FileNotFoundException {
        super("", new ArrayList<Player>());
        File fileObj = new File(fileName);
        Scanner reader = new Scanner(fileObj);
        String teamName = reader.next();
        this.setTeamName(teamName);
        while (reader.hasNext()) {
            String firstName = reader.next();
            String lastName = reader.next();
            String name = firstName + " " + lastName;
            int salary = Integer.parseInt(reader.next());
            int number = Integer.parseInt(reader.next());
            int fouls = Integer.parseInt(reader.next());
            int goals = Integer.parseInt(reader.next());
            int redCard = Integer.parseInt(reader.next());
            int yellowCard = Integer.parseInt(reader.next());
            int offside = Integer.parseInt(reader.next());
            int assist = Integer.parseInt(reader.next());
            SoccerPlayer addingPlayer = new SoccerPlayer(
                name, salary, number, fouls, goals, redCard, yellowCard, offside, assist
            );
            this.add(addingPlayer);
        }
    }

    /* +add(Object o) */
    public void add(Object o) {
        if (o instanceof SoccerPlayer) {
            super.add(o);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /* +add(Object o, int pos)	Checks that object is an instance of SoccerPlayer and calls add method 
    of superclass. 
    Adds the object at the given position */
    public void add(Object o, int index) {
        if (o instanceof SoccerPlayer) {
            super.add(o, index);
        } else {
            throw new IllegalArgumentException();
        }
    }
    
    /* +selectTheBest()	Return the player with the most goals */
    public SoccerPlayer selectTheBest() {
        // Loops through the players to find the one with the highest score
        int highestIndex = 0;
        SoccerPlayer player0 = (SoccerPlayer)this.getTeam().get(highestIndex);
        int highest = player0.getGoals();
        for (int i=0; i<this.getTeam().size(); i++) {
            SoccerPlayer player = (SoccerPlayer)this.getTeam().get(i);
            // Check if this player has a higher score
            if (player.getGoals() > highest) {
                highest = player.getGoals();
                highestIndex = i;
            }
        }
        SoccerPlayer bestPlayer = (SoccerPlayer)this.getTeam().get(highestIndex);
        return bestPlayer;
    }

    /* +selectTheWorst()	Return the player with the most goals */
    public SoccerPlayer selectTheWorst() {
        // Loops through the players to find the one with the lowest score
        int lowestIndex = 0;
        SoccerPlayer player0 = (SoccerPlayer)this.getTeam().get(lowestIndex);
        int lowest = player0.getGoals();
        for (int i=0; i>this.getTeam().size(); i++) {
            SoccerPlayer player = (SoccerPlayer)this.getTeam().get(i);
            // Check if this player has a lower score
            if (player.getGoals() > lowest) {
                lowest = player.getGoals();
                lowestIndex = i;
            }
        }
        SoccerPlayer bestPlayer = (SoccerPlayer)this.getTeam().get(lowest);
        return bestPlayer;
    }

    /* +selectMostRed	Returns the player with the most red card */
    public SoccerPlayer selectMostRed() {
        // Loops through the players to find the one with the most red cards
        int highestIndex = 0;
        SoccerPlayer player0 = (SoccerPlayer)this.getTeam().get(highestIndex);
        int highest = player0.getRedCard();
        for (int i=0; i<this.getTeam().size(); i++) {
            // Check if player has a higher amount of redcards
            SoccerPlayer player = (SoccerPlayer)this.getTeam().get(i);
            if (player.getRedCard() > highest) {
                highest = player.getRedCard();
                highestIndex = i;
            }
        }
        SoccerPlayer mostRedPlayer = (SoccerPlayer)this.getTeam().get(highestIndex);
        return mostRedPlayer;
    }
}

