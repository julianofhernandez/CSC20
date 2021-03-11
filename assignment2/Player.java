public class Player {
    private String name;
    private double salary;
    private int number;

    public Player(String name, double salary, int number) {
        this.name = name;
        this.salary = salary;
        this.number = number;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalary() {
        return this.salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean equals(Object other) {
        Player otherPlayer = (Player)other;
        if (this.name.equals(otherPlayer.getName())) {
                return true;
        } else {
            return false;
        }
    }

    /* +toString */
    public String toString() {
        return String.format("name: %s\n\tsalary: %f\n\tnumber: %d\n", this.getName(), 
        this.getSalary(), this.getNumber());
    }
}