public class SoccerPlayer extends Player {
    private int goals;
    private int fouls;
    private int redCard;
    private int yellowCard;
    private int offside;
    private int assist;

    public SoccerPlayer(String name, double salary, int number, int goals, int fouls, int redCard, int yellowCard, int offside, int assist) {
        super(name, salary, number);
        this.goals = goals;
        this.fouls = fouls;
        this.redCard = redCard;
        this.yellowCard = yellowCard;
        this.offside = offside;
        this.assist = assist;
    }

    public int getGoals() {
        return this.goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public int getFouls() {
        return this.fouls;
    }

    public void setFouls(int fouls) {
        this.fouls = fouls;
    }

    public int getRedCard() {
        return this.redCard;
    }

    public void setRedCard(int redCard) {
        this.redCard = redCard;
    }

    public int getYellowCard() {
        return this.yellowCard;
    }

    public void setYellowCard(int yellowCard) {
        this.yellowCard = yellowCard;
    }

    public int getOffside() {
        return this.offside;
    }

    public void setOffside(int offside) {
        this.offside = offside;
    }

    public int getAssist() {
        return this.assist;
    }

    public void setAssist(int assist) {
        this.assist = assist;
    }

    /* +toString */
    public String toString() {
        return String.format("name: %s\n\tsalary: %f\n\tnumber: %d\n\tgoals: %d\n\tfouls:" +
        " %d\n\tred cards: %d\n\tyellow cards: %d\n\toffside: %d\n\tassist: %d", this.getName(), 
        this.getSalary(), this.getNumber(), this.getGoals(), this.getFouls(), 
        this.getRedCard(), this.getYellowCard(), this.getOffside(), this.getAssist());
    }

    /* +equalsMethod accepts an object as its parameter */
    public boolean equals(Object other) {
        SoccerPlayer otherPlayer = (SoccerPlayer)other;
        // Checks only if the names are the same
        if (this.getName().equals(otherPlayer.getName())) {
            return true;
        } else {
            return false;
        }
    }

    /* Returns true or false based on whether the player should be removed from the game*/
    public Boolean pullOutPlayer() {
        if (this.fouls > 3 || this.yellowCard > 2) {
            return false;
        } else {
            return true;
        }
    }

}