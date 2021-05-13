public boolean areEven() {
    if (front == null) return false;
    return areEven(front);
}

areEven(ListNode current) {
    if (current != null) {
        if (current.data % 2 == 0) {
            return true && areEven(current.next);
        } else {
            return false;
        }
    } else {
        return true;
    }
}


areEven(ListNode current) {
    if(current != null) {
        int temp = current.data;
        current.data = current.next.data;
        current.next.data = temp;
        areEven(current.next.next);
    } else {
        return 
    }
}