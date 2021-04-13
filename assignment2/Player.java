//*************************************************
//author: Julian Hernandez
//date: 2/12/21
//title: Assignment 2
//description: Soccer and Basketball teams
//***************************************************

/**
* Player is a parent class for creating specific sport teams
* 
* @author Julian Hernandez
* @version v1.0
*/
public class Player {
    private String name;
    private double salary;
    private int number;

    /** The constructor for a player
    * @param name     Name of player
    * @param salary   Their starting salary
    * @param number   Their number on their team
    */
    public Player(String name, double salary, int number) {
        this.name = name;
        this.salary = salary;
        this.number = number;
    }
    
    /** Getter for name
     * @return String their name
     */
    public String getName() {
        return this.name;
    }
    
    /** Setter for name
    * @param name name of player
    */
    public void setName(String name) {
        this.name = name;
    }
    
    /** Getter for salary
     * @return their salary
    */
    public double getSalary() {
        return this.salary;
    }
    
    /** Setter for salary
    * @param salary   Their starting salary*/
    public void setSalary(double salary) {
        this.salary = salary;
    }
    
    /** Getter for number
     * @return The players number
    */
    public int getNumber() {
        return this.number;
    }
    
    /** Setter for number
    * @param number   Their number on their team*/
    public void setNumber(int number) {
        this.number = number;
    }
    
    /** Allows two players to be compared
    @param other    This is the player to compare against*/
    public boolean equals(Object other) {
        Player otherPlayer = (Player)other;
        if (this.name.equals(otherPlayer.getName())) {
                return true;
        } else {
            return false;
        }
    }

    /** +toString will print all the information about the player*/
    public String toString() {
        return String.format("name: %s\n\tsalary: %f\n\tnumber: %d\n", this.getName(), 
        this.getSalary(), this.getNumber());
    }
}