/*
Write a method countCommon that takes two Maps of <String, Integer> as parameters and returns the number of unique integer values that occur in both maps. Use one or more Sets to help you solve this problem.

For example, if one map contains the values [3, 7, 3, -1, 2, 3, 7, 2, 15, 15] and the other map contains the values [-5, 15, 2, -1, 7, 15, 36], your method should return 4 (because the integer values  -1, 2, 7, and 15 occur in both maps).
*/

import java.util.*;

public class q2 {
    public static void main(String[] args) {
        Map<String, Integer> h1 = new HashMap<String, Integer>();
        h1.put("a", new Integer(100));
        h1.put("b", new Integer(200));
        h1.put("c", new Integer(300));
        h1.put("d", new Integer(400));
        h1.put("e", new Integer(500));
        Map<String, Integer> h2 = new HashMap<String, Integer>();
        h2.put("a", new Integer(1));
        h2.put("b", new Integer(200));
        h2.put("c", new Integer(300));
        h2.put("d", new Integer(400));
        h2.put("e", new Integer(500));
        System.out.println(countCommon(h1, h2));
    }

    public static int countCommon(Map<String, Integer> map1, Map<String, Integer> map2) {
        HashSet<Integer> set1 = new HashSet<Integer>(map1.values());
        HashSet<Integer> set2 = new HashSet<Integer>(map2.values());
        int count = 0;

        for(int n : set2) {
            if(set1.contains(n))
                count++;
        }

        return count;
    }

}