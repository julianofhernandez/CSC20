class Main {

    public static void main(String args[]){
        System.out.println(Car.count);
        Car toyota = new Car("Camry","White",2019);
        System.out.println(toyota.count);
        toyota.count--;
        System.out.println(toyota.count);
        Car ford = new Car("Fiesta","Black",2000);
        ford.count++;
        System.out.println(Car.count);
    }
}