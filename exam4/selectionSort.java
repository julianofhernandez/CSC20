import java.util.*;

public class selectionSort {
    public static void main(String[] args) {
        System.out.println("Hi");
        int[] data = new int[]{30,1,5,3,2,0,17};
        System.out.println(Arrays.toString(data));
        selectionSort(data);
        System.out.println(Arrays.toString(data));

    }

public static void selectionSort(int[] data){
    int minIndex, minValue, tmp;
    for (int i= 0;i<data.length-1;i++){
        minValue = data[i];
        minIndex = i;
        for (int j=i+1;j<data.length;j++){
            if (data[j] < minValue) {
                // find the least minValue
                minValue = data[j];
                // find the index of the least minValue
                minIndex = j;
            }
        }
        tmp = data[i];
        data[i] = minValue;
        data[minIndex] = tmp;
    }
}
}

