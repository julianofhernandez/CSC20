import java.util.*;

public class Main{
  public static void main(String args[]){
      Car c = new Vehicle();}}  // declaration of object c

public class Vehicle{
    public Vehicle() {
        
    }
    public void move(){
        System.out.println("I am a Vehicle, I am moving");
    }
}

public class Car extends Vehicle{
    public void move(){
        System.out.println("I am a Car, I am on the road");
    }
}