
/**
* BasketBallPlayer is a subclass of Player that is setup to hold values and methods for basketball plays who 
can be managed within a BasketBallTeam class
* @author Julian Hernandez
* @version v1.0
*/
public class BasketBallPlayer extends Player implements Comparable {
    private int threePointer;
    private int turnOvers;
    private int assist;
    private int rebounds;
    private int twoPointer;

    /** The constructor for a BasketBallPlayer
    * @param name         Name of player
    * @param salary       Their starting salary
    * @param number       Their number on their team
    * @param threePointer Their number of three pointers
    * @param turnOvers    Their number of turn overs
    * @param assist       Their number of assists
    * @param rebounds     Their number of rebounds
    * @param twoPointer   Their number of two pointers
    */
    public BasketBallPlayer(String name, double salary, int number, int threePointer, int turnOvers, int assist, int rebounds, int twoPointer) {
        super(name, salary, number);
        this.threePointer = threePointer;
        this.turnOvers = turnOvers;
        this.assist = assist;
        this.rebounds = rebounds;
        this.twoPointer = twoPointer;
    }

    /** Getter for three pointers
     * @return Total three pointers for this player
    */
    public int getThreePointer() {
        return this.threePointer;
    }
    
    /** Setter for three pointers
     * @param threePointer The amount of three pointers to set
    */
    public void setThreePointer(int threePointer) {
        this.threePointer = threePointer;
    }
    
    /** Getter for turn overs
     * @return Turn overs
    */
    public int getTurnOvers() {
        return this.turnOvers;
    }
    
    /** Setter for turn overs
     * @param turnOvers What to set turnOvers to
    */
    public void setTurnOvers(int turnOvers) {
        this.turnOvers = turnOvers;
    }
    
    /** Getter for assists
     * @return assists
    */
    public int getAssist() {
        return this.assist;
    }
    
    /** Setter for assists
     * @param assist What to set assists to
    */
    public void setAssist(int assist) {
        this.assist = assist;
    }
    
    /** Getter for rebounds
     * @return Rebounds
    */
    public int getRebounds() {
        return this.rebounds;
    }
    
    /** Setter for rebounds
     * @param rebounds What to set rebounds to
    */
    public void setRebounds(int rebounds) {
        this.rebounds = rebounds;
    }
    
    /** Getter for two pointer
     * @return two pointers
    */
    public int getTwoPointer() {
        return this.twoPointer;
    }
    
    /** Setter for two pointer
     * @param twoPointer What to set two pointers to
    */
    public void setTwoPointer(int twoPointer) {
        this.twoPointer = twoPointer;
    }
    
    /** +equalsMethod accepts an object as its parameter 
    * @param other This is the other BasketBallPlayer to compare against*/
    public boolean equals(Object other) {
        BasketBallPlayer otherPlayer = (BasketBallPlayer)other;
        // Checks only if the names are the same
        if (this.getName().equals(otherPlayer.getName())) {
            return true;
        } else {
            return false;
        }
    }

    /** +toString 
    * @return Player stats*/
    public String toString() {
        return String.format("name: %s\n\tsalary: %f\n\tnumber: %d\n\tthree pointers: %d\n\tturn overs:" +
        " %d\n\tassists: %d\n\trebounds: %d\n\ttwo pointers: %d", this.getName(), this.getSalary(), 
        this.getNumber(), this.getThreePointer(), this.getTurnOvers(), this.getAssist(), 
        this.getRebounds(), this.getTwoPointer());
    }

    /** +getTotalPoints(): 
    * @return int returns the total points made by the player */
    public int getTotalPoints() {
        return this.threePointer*3 + this.twoPointer*2;
    }

    @Override
    public int compareTo(Object other) {
        BasketBallPlayer otherPlayer = (BasketBallPlayer)other;
        if (this.getTotalPoints() < otherPlayer.getTotalPoints()) {
            return -1;
        } else if (this.getTotalPoints() > otherPlayer.getTotalPoints()) {
            return 1;
        } else {
            return 0;
        }
    }
}