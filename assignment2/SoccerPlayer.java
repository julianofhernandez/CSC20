//************************************************
//author: Julian Hernandez
//date: 2/12/21
//title: Assignment 2
//description: Soccer and Basketball teams
//**************************************************

/**
* SoccerPlayer is a subclass of Player that is setup to hold values and methods for soccer plays who 
can be managed within a SoccerTeam class
* @author Julian Hernandez
* @version v1.0
*/
public class SoccerPlayer extends Player implements Comparable {
    private int goals;
    private int fouls;
    private int redCard;
    private int yellowCard;
    private int offside;
    private int assist;

    /** The constructor for a player
    * @param name         Name of player will be passed to the super class constructor
    * @param salary       Their starting salary will be passed to the super class constructor
    * @param number       Their number on their team will be passed to the super class constructor
    * @param goals        The number of goals for this player
    * @param fouls        The number of foals for this player
    * @param redCard      The number of redCard for this player
    * @param yellowCard   The number of yellowCard for this player
    * @param offside      The number of offside for this player
    * @param assist       The number of assist for this player
    */
    public SoccerPlayer(String name, double salary, int number, int goals, int fouls, int redCard, int yellowCard, int offside, int assist) {
        super(name, salary, number);
        this.goals = goals;
        this.fouls = fouls;
        this.redCard = redCard;
        this.yellowCard = yellowCard;
        this.offside = offside;
        this.assist = assist;
    }

    /** Getter for goals
     * @return goals
    */
    public int getGoals() {
        return this.goals;
    }
    
    /** Setter for goals
     * @param goals The goals we want to set
    */
    public void setGoals(int goals) {
        this.goals = goals;
    }
    
    /** Getter for fouls
     * @return fouls
    */
    public int getFouls() {
        return this.fouls;
    }
    
    /** Setter for fouls
     * @param fouls The fouls we want to be set
    */
    public void setFouls(int fouls) {
        this.fouls = fouls;
    }
    
    /** Getter for red cards
     * @return the total red cards
    */
    public int getRedCard() {
        return this.redCard;
    }
    
    /** Setter for red cards
     * @param redCard How many red cards we want this player to have
    */
    public void setRedCard(int redCard) {
        this.redCard = redCard;
    }
    
    /** Getter for yellow cards
     * @return the total yellow cards
    */
    public int getYellowCard() {
        return this.yellowCard;
    }
    
    /** Setter for yellow cards
     * @param yellowCard How many yellow cards we want set
    */
    public void setYellowCard(int yellowCard) {
        this.yellowCard = yellowCard;
    }
    
    /** Getter for offside
     * @return How many off sides this player has
    */
    public int getOffside() {
        return this.offside;
    }
    
    /** Setter for offside
     * @param offside How many offsides we want set
    */
    public void setOffside(int offside) {
        this.offside = offside;
    }
    
    /** Getter for assist
     * @return How many assists this player has
    */
    public int getAssist() {
        return this.assist;
    }
    
    /** Setter for assist
     * @param assist How many assists this player should have
    */
    public void setAssist(int assist) {
        this.assist = assist;
    }
    
    /** +toString 
     * @return The string of information for this player
    */
    public String toString() {
        return String.format("name: %s\n\tsalary: %f\n\tnumber: %d\n\tgoals: %d\n\tfouls:" +
        " %d\n\tred cards: %d\n\tyellow cards: %d\n\toffside: %d\n\tassist: %d", this.getName(), 
        this.getSalary(), this.getNumber(), this.getGoals(), this.getFouls(), 
        this.getRedCard(), this.getYellowCard(), this.getOffside(), this.getAssist());
    }

    /** +equalsMethod accepts an object as its parameter 
    * @param other    the other SoccerPlayer to compare against
    * @return   True is the players have the same name
    */
    public boolean equals(Object other) {
        SoccerPlayer otherPlayer = (SoccerPlayer)other;
        // Checks only if the names are the same
        if (this.getName().equals(otherPlayer.getName())) {
            return true;
        } else {
            return false;
        }
    }

    /** Returns true or false based on whether the player should be removed from the game
     * @return True is the player should be removed
    */
    public Boolean pullOutPlayer() {
        if (this.fouls > 3 || this.yellowCard > 2) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public int compareTo(Object other) {
        SoccerPlayer otherPlayer = (SoccerPlayer)other;
        if (this.getGoals() < otherPlayer.getGoals()) {
            return -1;
        } else if (this.getGoals() > otherPlayer.getGoals()) {
            return 1;
        } else {
            return 0;
        }
    }
}