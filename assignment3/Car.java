/**
* Car acts as the node class for the train linked list
* 
* @author Julian Hernandez
* @version v1.0
*/
public class Car {
    /** The factory the car is going to */
    public String factory;
    /** The stop where the factory is*/
    public int stop;
    /** The material on the car*/
    public String material;
    /** The next car*/
    Car next;

    /** The default constructor */
    public Car(){
        // default constructor
        this.next = null;
    }

    /** The constructor for when there is no next
    * @param fact the factory
    * @param s the stop
    * @param m the material
     */
    public Car(String fact, int s, String m){
        // regular constructor sets next to null
        this.factory = fact;
        this.stop = s;
        this.material = m;
        this.next = null;
    }

    /** The full constructor
    * @param fact the factory
    * @param s the stop
    * @param m the material
    * @param next the next car
     */
    public Car(String fact, int s, String m, Car next){
        // regular constructor sets this.next=next;
        this.factory = fact;
        this.stop = s;
        this.material = m;
        this.next = next;
    }
}