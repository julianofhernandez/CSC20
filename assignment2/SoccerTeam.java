//***********************************************
//author: Julian Hernandez
//date: 2/12/21
//title: Assignment 2
//description: Soccer and Basketball teams
//*************************************************
import java.util.*;
import java.io.*;

/**
* SoccerTeam is a subclass of Team that is specifically setup to hold SoccerPlayers
* 
* @author Julian Hernandez
* @version v1.0
*/
public class SoccerTeam extends Team {
    /** +constructor()	Reads the info for the players from a file and creates an object of 
    SoccerPlayer and then stores it in the  arrayList called team. Then call super constructor 
    and pass team name and array list.
    * @param fileName The file to pull soccer players from
    */
    public SoccerTeam(String fileName) throws FileNotFoundException {
        super("", new ArrayList<Player>());
        File fileObj = new File(fileName);
        Scanner reader = new Scanner(fileObj);
        String teamName = reader.next();
        this.setTeamName(teamName);
        while (reader.hasNext()) {
            try {
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
            } catch (Exception e) {
                System.out.println("Failed to create player from file, please make sure it is formatted correctly");
            }
        }
    }

    /** +add(Object o) 
     * @param o Object to compare against
    */
    public void add(Object o) {
        if (o instanceof SoccerPlayer) {
            super.add(o);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /** +add(Object o, int pos)	Checks that object is an instance of SoccerPlayer and calls add method 
    of superclass. Adds the object at the given position
    * @param o Object to add
    * @param index Index to add object o at
    */
    public void add(Object o, int index) {
        if (o instanceof SoccerPlayer) {
            super.add(o, index);
        } else {
            throw new IllegalArgumentException();
        }
    }
    
    /** +selectTheBest()	Return the player with the most goals 
    * @return the Soccer player with the highest score
    */
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

    /** +selectMostRed	Returns the player with the most red card 
     * @return The soccer player with the most red
    */
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

