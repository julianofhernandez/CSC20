import java.util.*;

public class BasketBallTeam {
    /* +constructor()	Reads the info for the players from a file and creates an object of 
    BasketBallPlayer and then stores it in the  arrayList called team. Then call super constructor 
    and pass team name and array list.*/
    public BasketBallTeam() {

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
        int highest = this.team.get(highestIndex).getTotalPoints()
        for (int i=0; i<this.team.size(); i++) {
            // Check if this player has a higher score
            if (this.team.get(i).getTotalPoints() > highest) {
                highest = this.team.get(i).getTotalPoints();
                highestIndex = i;
            }
        }
        return this.team.get(highestIndex);
    }

    /* +selectMostRebounds	Returns the player with the most rebounds */
    public BasketBallPlayer selectMostRebounds() {
        // Loops through the players to find the one with the most rebounds
        int highestIndex = 0;
        int highest = this.team.get(highestIndex).getRebounds()
        for (int i=0; i<this.team.size(); i++) {
            // Check if player has a higher amount of redcards
            if (this.team.get(i).getRebounds() > highest) {
                highest = this.team.get(i).getRebounds();
                highestIndex = i;
            }
        }
        return this.team.get(highestIndex);
    }
}

