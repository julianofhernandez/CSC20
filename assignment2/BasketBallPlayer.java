public class BasketBallPlayer extends Player {
    private int threePointer;
    private int turnOvers;
    private int assist;
    private int rebounds;
    private int twoPointers;

    public BasketBallPlayer(String name, double salary, int number, int three, int turnover, int assist, int   rebound, int two, String team) {
        super(name, salary, number);
        this.threePointer = threePointer;
        this.turnOvers = turnOvers;
        this.assist = assist;
        this.rebounds = rebounds;
        this.twoPointers = twoPointers;
    }

    public int getthreePointer() {
        return this.threePointer;
    }

    public void setthreePointer(int threePointer) {
        this.threePointer = threePointer;
    }

    public int getTurnOvers() {
        return this.turnOvers;
    }

    public void setTurnOvers(int turnOvers) {
        this.turnOvers = turnOvers;
    }

    public int getAssist() {
        return this.assist;
    }

    public void setAssist(int assist) {
        this.assist = assist;
    }

    public int getRebounds() {
        return this.rebounds;
    }

    public void setRebounds(int rebounds) {
        this.rebounds = rebounds;
    }

    public int getTwoPointers() {
        return this.twoPointers;
    }

    public void setTwoPointers(int twoPointers) {
        this.twoPointers = twoPointers;
    }

    public boolean equals(Object other) {
        BasketBallPlayer otherPlayer = (BasketBallPlayer)other;
        super.equals(other);
    }
// +toString
// +getTotalPoints(): int// returns the total points made by the player

}