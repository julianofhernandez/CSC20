public class SoccerTeam {
    // +constructor()	Reads the info for the players from a file and creates an object of SoccerPlayer and then stores it in the  arrayList called team. Then call super constructor and pass team name and array list.
    public SoccerTeam() {

    }
    // +add(Object o)
    public void add(Object o) {
        if (o instanceof SoccerPlayer) {
            super.add(o);
        } else {
            throw new IllegalArgumentException();
        }
    }
    // +add(Object o, int pos)	Checks that object is an instance of SoccerPlayer and calls add method of superclass.
    // Adds the object at the given position
    public void add(Object o, int index) {
        if (o instanceof SoccerPlayer) {
            super.add(o, index);
        } else {
            throw new IllegalArgumentException();
        }
    }
    
    // +selectTheBest()	Return the player with the most goals
    public SoccerPlayer selectTheBest() {
        // Loops through the players to find the one with the highest score
        int highestIndex = 0;
        int highest = this.team.get(highestIndex).getGoals()
        for (int i=0; i<this.team.size(); i++) {
            // Check if this player has a higher score
            if (this.team.get(i).getGoals() > highest) {
                highest = this.team.get(i).getGoals();
                highestIndex = i;
            }
        }
        return this.team.get(highestIndex);
    }

    // +selectMostRed	Returns the player with the most red card
    public SoccerPlayer selectMostRed() {
        // Loops through the players to find the one with the most red cards
        int highestIndex = 0;
        int highest = this.team.get(highestIndex).getRedCard()
        for (int i=0; i<this.team.size(); i++) {
            // Check if player has a higher amount of redcards
            if (this.team.get(i).getRedCard() > highest) {
                highest = this.team.get(i).getRedCard();
                highestIndex = i;
            }
        }
        return this.team.get(highestIndex);
    }
}

