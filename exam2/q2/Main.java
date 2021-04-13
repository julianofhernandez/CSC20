public class Main {
    public static void main(String[] args) {
        Dice d = new Dice();
        System.out.println(d);                             // Print dice, initially “(1)”
        d.roll();                                                       // Assigns a random value 1 to 6
        System.out.println(d);                                  // Prints current value of dice
    }
}