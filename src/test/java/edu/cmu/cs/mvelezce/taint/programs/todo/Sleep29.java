package edu.cmu.cs.mvelezce.taint.programs.todo;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Sleep29 {

    public static boolean A;
    public static boolean B;
    public static boolean C;
    public static boolean D;

    public static void main(String[] args) throws InterruptedException {
        A = Boolean.valueOf(args[0]);
        B = Boolean.valueOf(args[1]);
        C = Boolean.valueOf(args[2]);
        D = Boolean.valueOf(args[3]);

        int a;
        int b;
        int c;
        boolean d;

        if(A) {
            a = 0;
        }
        else {
            a = 10;
        }

        if(B) {
            b = 0;
        }
        else {
            b = 10;
        }

        if(C) {
            c = 0;
        }
        else {
            c = 10;
        }

        if(D) {
            d = true;
        }
        else {
            d = false;
        }

        method1(a, b, c, d);
    }


    public static void method1(int a, int b, int c, boolean d) throws InterruptedException {
        Thread.sleep(100);

        if(a > 0) {
            Thread.sleep(200);
            method4(d);
            method2(a, d);
        }

        if(b >= 0) {
            method4(d);
            method3(b);
        }

        if(c > 0) {
            Thread.sleep(300);
            method4(d);
            method2(c, d);
        }
    }

    public static void method2(int a, boolean d) throws InterruptedException {
        if(a > 0) {
            Thread.sleep(a * 100);
            method4(d);
        }
        else {
            Thread.sleep(400);
            method4(d);
        }
    }

    public static void method3(int b) throws InterruptedException {
        if(b > 0) {
            Thread.sleep(500);
        }
        else {
            Thread.sleep(200);
        }
    }

    public static void method4(boolean d) throws InterruptedException {
        if(d) {
            Thread.sleep(500);
        }
        else {
            Thread.sleep(200);
        }
    }

}
