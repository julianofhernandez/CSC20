import java.util.*;

public class Main {
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<String>();
        list.add("be");
        list.add("be");
        list.add("is");
        list.add("not");
        list.add("or");
        list.add("question");
        list.add("question");
        list.add("that");
        list.add("the");
        list.add("to");
        list.add("to");
        System.out.println(removeDuplicates(list));
        // {"be", "be", "is", "not", "or", "question", "that", "the", "to", "to"};
    }

    public static ArrayList<String> removeDuplicates(ArrayList<String> arr) {
        for (int i=arr.size()-1; i>0; i--) {
            if(arr.get(i).equals(arr.get(i-1))) {
                arr.remove(i);
            }
        }
        return arr;
    }
}