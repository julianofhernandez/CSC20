import java.util.*;

public class q3 {
    public static void main(String[] args) {
        Stack<Integer> stack1 = new Stack<Integer>();
        stack1.push(4);
        stack1.push(4);
        stack1.push(4);
        stack1.push(4);
        stack1.push(5);
        int result = addUpStack(stack1);
        System.out.println(stack1);
        System.out.println(result);
    }
    public static int addUpStack(Stack<Integer> s) {
        Queue<Integer> q = new LinkedList<Integer>();
        int size = s.size(); int num = 0;
        int total = 0;
        int item;
        for (int i = 1; i<=size; i++) {
            item = s.pop();
            total += item;
            q.add(item);
        }
        for (int i = 1; i<=size; i++) s.push(q.remove());
        for (int i = 1; i<=size; i++) q.add(s.pop());
        for (int i = 1; i<=size; i++) {
            num = q.remove();
            s.push(num);
        }
        return total;
    }
}

