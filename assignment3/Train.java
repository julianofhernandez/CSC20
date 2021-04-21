import java.util.*;
import java.io.*;

/**
* Train class keeps track of the LinkedList of cars
* 
* @author Julian Hernandez
* @version v1.0
*/
public class Train {
    /** The next car, which starts are null */
    public Car front = null;

    /** constructor: reads each train car details from the file into the LinkedList 
    * @param carFile The file to read the cars from
    * @throws FileNotFoundException This is due to opening a scanner on a file
    */
    public Train(String carFile) throws FileNotFoundException {
        File fileObj = new File(carFile);
        Scanner reader = new Scanner(fileObj);
        String inline;
        String factoryName; int stopNumber; String materialName;
        while (reader.hasNextLine()) {
            inline = reader.nextLine();
            String tokens[] = inline.split(",");
            factoryName = tokens[0];
            stopNumber = Integer.parseInt(tokens[1]);
            materialName = tokens[2];
            this.attach(factoryName, stopNumber, materialName);
        }
    }

    /** remove a car given factoryName, this should delete the node in the LinkedList. 
    * @param factoryName The factory to detach the cars for
    */
    public void detach(String factoryName) {
        Car prev = front;
        Car current = this.front.next;
        while (current != null) {
            if (current.factory.equals(factoryName)) {
                prev.next = current.next;
                current= current.next;
            } else {
                prev = prev.next;
                current = current.next;
            }
        }
        if (this.front.factory.equals(factoryName)) {
            if (this.front.next != null) {
                this.front = this.front.next;
            }
        }
    }

    /** add a new car(node) to the LinkedList
    * @param factoryName the factory
    * @param stopNumber the stop number
    * @param materialName the material
    */ 
    public void attach(String factoryName, int stopNumber, String materialName) {
        // If list is empty just add it
        Car current = this.front;
        Car prev = this.front;
        if (current == null) {
            Car newCar = new Car(factoryName, stopNumber, materialName, current);
            this.front = newCar;
            return;
        }
        // if theres any items
        current = current.next;
        if (current != null) {
            while (current.stop < stopNumber && current.next != null) {
                prev = current;
                current = current.next;
            }
            if (current.stop < stopNumber && current.next == null) {
                prev = current;
                current = current.next;
            }
        }
        if (stopNumber < this.front.stop) {
            Car newCar = new Car(factoryName, stopNumber, materialName, this.front);
            this.front = newCar;
        } else {
            Car newCar = new Car(factoryName, stopNumber, materialName, current);
            prev.next = newCar;
        }
        
    }


    /** search for cars that correspond the given factory name. Note there may be more than one car so you may use helper method that follows 
    *@param factoryName the name of the factory to search for goods
    */
    public void search(String factoryName) {
        List<String> materials = this.getCars(factoryName);
        print("The material in the car is ");
        String print = "";
        for (String material : materials) {
            print = print + material + " and ";
        }
        print = print.substring(0, print.length() - 5);
        println(print);
        println();
    }

    /** material name lookup given factory name
    * @param factoryName the name of the factory to search for cars
    * @return The list of cars that are going to that factory
    */
    public List<String> getCars(String factoryName) {
        List<String> list= new ArrayList<String>();
        Car current = this.front;
        while (current.next != null) {
            if (current.factory.equals(factoryName)) {
                list.add(current.material);
            }
            current = current.next;
        }
        return list;
    }

    /** display all the train cars in sorted order */
    public void displayTrainCars() {
        Car current = this.front;
        while (current != null) {
            println(String.format("%-15s%d\t%s", current.factory, current.stop, current.material));
            current = current.next;
        }
        println();
    }

    /** reads from update.txt and merges it with the new train car. Merge two LinkedLists and should maintain the sorted order. 
    * @param update the file to read the new Cars from
    * @throws FileNotFoundException This is due to opening a scanner on a file
    */
    public void merge(String update) throws FileNotFoundException {
        println("Displaying the existing cars");
        this.displayTrainCars();
        Train newTrain = new Train(update);
        println("Displaying the new cars");
        newTrain.displayTrainCars();
        Car current = newTrain.front;
        while (current != null) {
            this.attach(current.factory, current.stop, current.material);
            current = current.next;
        }
    }

    /**This is just to simplify the print process 
     * @param input String to print
    */
    public static void print(String input) {
		System.out.print(input);
	}

    /**This is just to simplify the println process 
     * @param input String to print
    */
	public static void println(String input) {
		System.out.println(input);
	}

    /**This is just to simplify the println process
    */
    public static void println() {
		System.out.println("");
	}
}


