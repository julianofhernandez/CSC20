public class q1 {

public static boolean checkSame(HashSet<Integer>s1,HashSet<Integer>s2){
    ArrayList<Integer> a1= new ArrayList<>();
    for (int i: s1)
        a1.add(i);
    ArrayList<Integer> a2= new ArrayList<>();
    for (int i: s2)
        a2.add(i);
    if (a1.size()!=a2.size()) return false;
    else {
    for (int i=0;i<a1.size();i++)
        if (a1.get(i)!=a2.get(i))
            return false;
    }
        return true;
    }
}