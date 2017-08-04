package edu.cmu.cs.mvelezce.taint.programs.todo;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Union6 {

    public static boolean A = false;
    public static boolean B = false;
    public static boolean C = false;
    public static boolean D = false;
    public static boolean E = false;

    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(1);
        C = Boolean.valueOf(args[2]);

        boolean c;

        if(C) {
            c = true;
        }
        else {
            c = false;
        }

        if(A) {
            Thread.sleep(2);
            m1(c);
        }

        if(B) {
            Thread.sleep(3);
            m1(c);
        }

        m1(c);
    }

    public static void m1(boolean x) throws InterruptedException {
        Thread.sleep(4);

        if(x) {
            Thread.sleep(5);
            int a = 0;
            a = a + 5;
            a = a - 2;
            m2(x);
        }

        boolean d;

        if(D) {
            d = true;
        }
        else {
            d = false;
        }

        if(d) {
            m2(d);
        }
    }

    public static void m2(boolean y) throws InterruptedException {
        if(E) {
            int a = 0;
            a = a + 5;
            a = a - 2;

            Union5.m1(y);

        }
    }

}
