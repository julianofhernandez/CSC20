public class BasketBallPlayer extends Player {
    private int threePointer;
    private int turnOvers;
    private int assist;
    private int rebounds;
    private int twoPointer;

    public BasketBallPlayer(String name, double salary, int number, int threePointer, int turnOvers, int assist, int rebounds, int twoPointer) {
        super(name, salary, number);
        this.threePointer = threePointer;
        this.turnOvers = turnOvers;
        this.assist = assist;
        this.rebounds = rebounds;
        this.twoPointer = twoPointer;
    }

    public int getThreePointer() {
        return this.threePointer;
    }

    public void setThreePointer(int threePointer) {
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

    public int getTwoPointer() {
        return this.twoPointer;
    }

    public void setTwoPointer(int twoPointer) {
        this.twoPointer = twoPointer;
    }

    /* +equalsMethod accepts an object as its parameter */
    public boolean equals(Object other) {
        BasketBallPlayer otherPlayer = (BasketBallPlayer)other;
        // Checks only if the names are the same
        if (this.getName().equals(otherPlayer.getName())) {
            return true;
        } else {
            return false;
        }
    }

    /* +toString */
    public String toString() {
        return String.format("name: %s\nsalary: %d\nnumber: %d\nthree pointers: %d\nturn overs:" +
        " %d\nassists: %d\nrebounds: %d\ntwo pointers: %d", this.getName(), this.getNumber(), 
        this.getThreePointer(), this.getTurnOvers(), this.getAssist(), this.getRebounds(), this.getTwoPointer());
    }

    /* +getTotalPoints(): int// returns the total points made by the player */
    public int getTotalPoints() {
        return this.threePointer*3 + this.twoPointer*2;
    }
}