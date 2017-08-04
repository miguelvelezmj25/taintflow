package edu.cmu.cs.mvelezce.taint.programs.todo;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Sleep3 {

    public static boolean A = false;
    public static boolean B = false;

    public static void main(String[] args) throws InterruptedException {
        // Region program start
        System.out.println("main");

//        boolean a = Boolean.valueOf(args[0]);
//        boolean b = Boolean.valueOf(args[1]);
        A = Boolean.valueOf(args[0]);
        B = Boolean.valueOf(args[1]);

        boolean a;
        boolean b;

        if(A) {
            a = true;
        }
        else {
            a = false;
        }

        if(B) {
            b = true;
        }
        else {
            b = false;
        }

        Thread.sleep(200);

        if(a) {
            Thread.sleep(600);
            Sleep3.method1(a);
        }

        Thread.sleep(100);

        if(b) {
            Thread.sleep(600);
            Sleep3.method2(b);
        }

    }

    public static void method1(boolean A) throws InterruptedException {
        System.out.println("method1");
        boolean a = A;
        Thread.sleep(200);

        if(a) {
            Thread.sleep(600);
        }

        Thread.sleep(100);
    }

    public static void method2(boolean B) throws InterruptedException {
        System.out.println("method2");
        boolean b = B;

        if(b) {
            Thread.sleep(600);
        }

        Thread.sleep(200);
    }

}
