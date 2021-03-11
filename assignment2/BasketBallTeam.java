import java.util.*;
import java.io.*;

public class BasketBallTeam extends Team{
    /* +constructor()	Reads the info for the players from a file and creates an object of 
    BasketBallPlayer and then stores it in the  arrayList called team. Then call super constructor 
    and pass team name and array list.*/
    public BasketBallTeam(String fileName) throws FileNotFoundException{
        super("", new ArrayList<Player>());
        File fileObj = new File(fileName);
        Scanner reader = new Scanner(fileObj);
        String teamName = reader.next();
        this.setTeamName(teamName);
        while (reader.hasNext()) {
            String firstName = reader.next();
            String lastName = reader.next();
            String name = firstName + lastName;
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
        }
    }

    /* +add(Object o) */
    public void add(Object o) {
        if (o instanceof BasketBallPlayer) {
            super.add(o);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /* +add(Object o, int pos)	Checks that object is an instance of BasketBallPlayer and calls add method 
    of superclass. 
    Adds the object at the given position */
    public void add(Object o, int index) {
        if (o instanceof BasketBallPlayer) {
            super.add(o, index);
        } else {
            throw new IllegalArgumentException();
        }
    }
    
    /* +selectTheBest()	Return the player with the most points */
    public BasketBallPlayer selectTheBest() {
        // Loops through the players to find the one with the highest score
        int highestIndex = 0;
        BasketBallPlayer player0 = (BasketBallPlayer)this.getTeam().get(highestIndex);
        int highest = player0.getTotalPoints();
        for (int i=0; i<this.getTeam().size(); i++) {
            BasketBallPlayer player = (BasketBallPlayer)this.getTeam().get(highestIndex);
            // Check if this player has a higher score
            if (player.getTotalPoints() > highest) {
                highest = player.getTotalPoints();
                highestIndex = i;
            }
        }
        BasketBallPlayer bestPlayer = (BasketBallPlayer)this.getTeam().get(highestIndex);
        return bestPlayer;
    }
    
    /* +selectTheWorst()	Return the player with the most points */
    public BasketBallPlayer selectTheWorst() {
        // Loops through the players to find the one with the highest score
        int lowestIndex = 0;
        BasketBallPlayer player0 = (BasketBallPlayer)this.getTeam().get(lowestIndex);
        int lowest = player0.getTotalPoints();
        for (int i=0; i<this.getTeam().size(); i++) {
            BasketBallPlayer player = (BasketBallPlayer)this.getTeam().get(lowestIndex);
            // Check if this player has a higher score
            if (player.getTotalPoints() < lowest) {
                lowest = player.getTotalPoints();
                lowestIndex = i;
            }
        }
        BasketBallPlayer worstPlayer = (BasketBallPlayer)this.getTeam().get(lowestIndex);
        return worstPlayer;
    }
    
    /* +selectMostRebounds	Returns the player with the most rebounds */
    public BasketBallPlayer selectMostRebounds() {
        // Loops through the players to find the one with the most rebounds
        int highestIndex = 0;
        BasketBallPlayer player0 = (BasketBallPlayer)this.getTeam().get(highestIndex);
        int highest = player0.getRebounds();
        for (int i=0; i<this.getTeam().size(); i++) {
            BasketBallPlayer player = (BasketBallPlayer)this.getTeam().get(highestIndex);
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

