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