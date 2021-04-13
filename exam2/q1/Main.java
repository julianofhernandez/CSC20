
public class Main{
    public static void main(String [] args){
        // B one = new C();
        Object two= new B();
        A three = new D();
        // ((C) one).method3;// ______________________________
        // two.method2();// ______________________________
        three.method2();// ______________________________
        // one.method3();// ______________________________
        // ((C) three).method1(); //______________________________
 
  }
}

class A {
            public void method2() {
                  System.out.println("A 2");
                  method3();}
public void method3() {
System.out.println("A 3"); }

}

class B extends A {
    public void method3() {
        System.out.println("B 3");}
      }

      class C extends B {
      public void method1() {
        System.out.println("C 1");}
    public void method3() {
        System.out.println("C 3"); super.method3();}
}

class D extends A {
    public void method1() {
        System.out.println("D 1"); super.method3();}
    public void method3() {
        System.out.println("D 3"); }
} 


/*

((C) one).method3// compile error q1.java:37: error: not a statement
two.method2();// compile error q1.java:38: error: cannot find symbol
three.method2();//A 2/D 3
one.method3();// compile error error: cannot find symbo
((C) three).method1(); runtime error //Exception in thread "main" java.lang.ClassCastException: class D cannot be cast to class C (D and C are in unnamed module of loader com.sun.tools.javac.launcher.Main$MemoryClassLoader @588df31b)

*/