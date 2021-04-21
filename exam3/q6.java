public static ArrayList<String> doSomething(ArrayList<String> a){
    int size = a.size();
    for (int i=0;i<size;i++){
        a.add(i+size,a.get(size-i-1));
    }
    return a;
}