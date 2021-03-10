public class SoccerPlayer extends Player {
    private int goals;
    private int fouls;
    private int redCard;
    private int yellowCard;
    private int offside;
    private int assist;

    public SoccerPlayer(String name, double salary, int number, int three, int turnover, int redCard, int   rebound, int two, String team) {
        super(name, salary, number);
        this.goals = goals;
        this.fouls = fouls;
        this.redCard = redCard;
        this.yellowCard = yellowCard;
        this.offside = offside;
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


    // +toString method
    // +equalsMethod// accepts an object as its parameter

    public Boolean pullOutPlayer() {
        if (this.fouls > 3 || this.yellowCard > 2) {
            return false;
        } else {
            return true;
        }
    }

}