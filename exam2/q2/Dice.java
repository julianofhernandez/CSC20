import java.util.*;

public class Dice {
    private int roll = 0;
    public String toString() {
        if (this.roll == 0) {
            return "You haven't rolled yet";
        } else {
            return "You rolled " + this.roll;
        }
    }

    public void roll() {
        Random rand = new Random();
        this.roll = rand.nextInt(5) + 1;
    }
}