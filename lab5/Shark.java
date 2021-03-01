public class Shark extends Critter {
    public int moves = 1;
    public int inDir = 1;
    
    public Direction getMove() {
        Direction dir = Direction.CENTER;
        if (this.moves >= inDir) {
            if (this.inDir % 2 == 1) {
                dir = Direction.NORTH;
            }
            if (this.inDir % 2 == 0) {
                dir = Direction.SOUTH;
            }
            this.inDir++;
            this.moves = 1;
        } else {
            if (this.inDir % 2 == 1) {
                dir = Direction.NORTH;
            }
            if (this.inDir % 2 == 0) {
                dir = Direction.SOUTH;
            }
            this.moves++;
        }
        
        return dir;
    }
}