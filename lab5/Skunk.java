public class Skunk extends Critter {
   private int  moves;
   private  boolean  hungry;

   public  void  Skunk()  {    //  constructor
      this.hungry  =  false;
   }
        
        public  boolean  eat()  {
                this.hungry  =  true;
                return  true;
        }
        
        public  Direction  getMove()  {
                this.moves++;
                if  (this.moves  >=  3)  {
                        this.moves  =  0;
                }
                if  (this.moves  ==  1  ||  this.moves  ==  2)  {
                        return  Direction.WEST;
                }  else  if  (this.hungry)  {
                        return  Direction.SOUTH;
                }  else  if  (!this.hungry)  {
                        return  Direction.NORTH;
                }
                return  Direction.CENTER;
        }
        
        public  String  toString()  {
                return  "w";
        }
}