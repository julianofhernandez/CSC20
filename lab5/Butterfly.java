public class Butterfly extends Critter {
    public int moves = 0;
    public int alternator = 0;
    
    public boolean eat() {
        return false;
    }

    public Attack fight(String opponent) {
        return Attack.FORFEIT;
    }

    public Color getColor() {
        return Color.YELLOW;
    }

    public Direction getMove() {
        this.moves++;
        this.alternator++;
        if (this.moves==1) {
            return Direction.NORTH;
        } else if (this.moves==2) {
            return Direction.WEST;
        } else if (this.moves==3) {
            return Direction.NORTH;
        } else {
            this.moves=0;
            return Direction.EAST;
        }
    }

    public String toString() {
        if (this.alternator % 2 == 0) {
            return "x";
        } else {
            return "-";
        }
    }
}