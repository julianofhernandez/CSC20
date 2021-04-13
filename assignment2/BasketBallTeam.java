//***********************************************
//author: Julian Hernandez
//date: 2/12/21
//title: Assignment 2
//description: Soccer and Basketball teams
//*************************************************
import java.util.*;
import java.io.*;

/**
* BasketBallTeam is a subclass of Team that is specifically setup to hold BasketBallPlayers
* 
* @author Julian Hernandez
* @version v1.0
*/
public class BasketBallTeam extends Team{
    /** +constructor()	Reads the info for the players from a file and creates an object of 
    BasketBallPlayer and then stores it in the  arrayList called team. Then call super constructor 
    and pass team name and array list.
    @param fileName The file to read palyers from*/
    public BasketBallTeam(String fileName) throws FileNotFoundException{
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
                int threePointer = Integer.parseInt(reader.next());
                int turnOvers = Integer.parseInt(reader.next());
                int assist = Integer.parseInt(reader.next());
                int rebounds = Integer.parseInt(reader.next());
                int twoPointer = Integer.parseInt(reader.next());
                BasketBallPlayer addingPlayer = new BasketBallPlayer(
                    name, salary, number, threePointer, turnOvers, assist, rebounds, twoPointer
                );
                this.add(addingPlayer);
            } catch (Exception e) {
                System.out.println("Failed to create player from file, please make sure it is formatted correctly");
            }
        }
    }

    /** +add(Object o) 
     * @param o The object to add
    */
    public void add(Object o) {
        if (o instanceof BasketBallPlayer) {
            super.add(o);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /** +add(Object o, int pos)	Checks that object is an instance of BasketBallPlayer and calls add method 
    of superclass. Adds the object at the given position 
    * @param o The object to add
    * @param index The index to add this object at
    */
    public void add(Object o, int index) {
        if (o instanceof BasketBallPlayer) {
            super.add(o, index);
        } else {
            throw new IllegalArgumentException();
        }
    }
    
    /** +selectTheBest()	Return the player with the most points
     * @return The BasketBallPlayer with the highest score
     */
    public BasketBallPlayer selectTheBest() {
        // Loops through the players to find the one with the highest score
        int highestIndex = 0;
        BasketBallPlayer player0 = (BasketBallPlayer)this.getTeam().get(highestIndex);
        int highest = player0.getTotalPoints();
        for (int i=0; i<this.getTeam().size(); i++) {
            BasketBallPlayer player = (BasketBallPlayer)this.getTeam().get(i);
            // Check if this player has a higher score
            if (player.getTotalPoints() > highest) {
                highest = player.getTotalPoints();
                highestIndex = i;
            }
        }
        BasketBallPlayer bestPlayer = (BasketBallPlayer)this.getTeam().get(highestIndex);
        return bestPlayer;
    }
    
    /** +selectTheWorst()	Return the player with the most points 
     * @return BasketBallPlayer with the lowest score
    */
    public BasketBallPlayer selectTheWorst() {
        // Loops through the players to find the one with the highest score
        int lowestIndex = 0;
        BasketBallPlayer player0 = (BasketBallPlayer)this.getTeam().get(lowestIndex);
        int lowest = player0.getTotalPoints();
        for (int i=0; i<this.getTeam().size(); i++) {
            BasketBallPlayer player = (BasketBallPlayer)this.getTeam().get(i);
            // Check if this player has a higher score
            if (player.getTotalPoints() < lowest) {
                lowest = player.getTotalPoints();
                lowestIndex = i;
            }
        }
        BasketBallPlayer worstPlayer = (BasketBallPlayer)this.getTeam().get(lowestIndex);
        return worstPlayer;
    }
    
    /** +selectMostRebounds	Returns the player with the most rebounds 
     * @return BacketBallPlayer with the most rebounds
    */
    public BasketBallPlayer selectMostRebounds() {
        // Loops through the players to find the one with the most rebounds
        int highestIndex = 0;
        BasketBallPlayer player0 = (BasketBallPlayer)this.getTeam().get(highestIndex);
        int highest = player0.getRebounds();
        for (int i=0; i<this.getTeam().size(); i++) {
            BasketBallPlayer player = (BasketBallPlayer)this.getTeam().get(i);
            // Check if player has a higher amount of redcards
            if (player.getRebounds() > highest) {
                highest = player.getRebounds();
                highestIndex = i;
            }
        }
        BasketBallPlayer mostReboundsPlayer = (BasketBallPlayer)this.getTeam().get(highestIndex);
        return mostReboundsPlayer;
    }
}

