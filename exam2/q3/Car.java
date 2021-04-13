public class Car {
    public static int count = 0;
    private String model;
    private int year;
    private String color;
    public Car(String model, String color, int year){
            this.year=year; this.color=color; this.model=model; count++;
}   
}
