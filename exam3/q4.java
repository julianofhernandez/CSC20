import java.util.*;

public class q3 {
    public class ListNode {
        public int data;       // data stored in this node
        public ListNode next;  // a link to the next node in the list

        public ListNode() { ... }
        public ListNode(int data) { ... }
        public ListNode(int data, ListNode next) { ... }
    }
    
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

}

