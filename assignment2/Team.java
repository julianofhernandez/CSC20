//***********************************************
//author: Julian Hernandez
//date: 2/12/21
//title: Assignment 2
//description: Soccer and Basketball teams
//*************************************************
import java.util.*;

/**
* Team is a parent class for creating specific sport teams
* 
* @author Julian Hernandez
* @version v1.0
*/
public class Team {
    private String teamName;
    private ArrayList<Player> team;

    /** Constructs the class
    @param teamName sets the name of this team
    @param team accepts an ArrayList of Players and sets this team to those
    */
    public Team(String teamName, ArrayList<Player> team) {
        this.teamName = teamName;
        this.team = team;
    }
    
    /**  +getter method	
     * @return the object team of ArrayList type
    */
    public ArrayList<Player> getTeam() {
        return this.team;
    }

    /** Setter for teamName
     * @param teamName  TeamName value
     */
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    /** Setter for team
     * @param team  ArrayList containing the players to set
     */
    public void setTeam(ArrayList<Player> team) {
        this.team = team;
    }

    /** +add(Object o)	Adds the object to array list
     * @param o Object to be added*/ 
    public void add(Object o) {
        Player object = (Player)o;
        this.team.add(object);
    }
    
    /**+add(Object o, int pos)	Adds the object at the given position in arraylist
     * @param o Object to be added
     * @param index Index to add object at*/ 
    public void add(Object o, int index) {
        Player object = (Player)o;
        this.team.add(index, object);
    }

    /** +remove(int index)	Removes the object(Player) from the list at index
     * @param index What index player to remove
    */
    public void remove(int index) {
        this.team.remove(index);
    }
    
    /** +equals(Object other)	Returns true if the team is the same as the team other. 
    * Need a loop to compare all the players. 
    * @param other Which other player to compare against
    * @return True if the teams are the same
    */
    public boolean equals(Object other) {
        Team otherTeam = (Team)other;
        // Checks if the teams are the same size
        if (this.team.size() != otherTeam.getTeam().size()) {
            return false;
        }
        // Loops through the players to make sure they are the same
        for (int i=0; i<this.team.size(); i++) {
            // Check if players are not equal
            if (!this.team.get(i).equals(otherTeam.getTeam().get(i))) {
                return false;
            }
        }
        // If the size is the same and all players are the same, then return true
        return true;
    }
    
    /** +toString	Returns a string representing all the players in the team. A for loop is needed 
     * @return String value of team
    */
    public String toString() {
        String returnString = "";
        for (int i=0; i<this.team.size(); i++) {
            returnString += this.team.get(i).toString();
        }
        return returnString;
    }

}