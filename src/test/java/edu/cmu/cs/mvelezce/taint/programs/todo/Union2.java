package edu.cmu.cs.mvelezce.taint.programs.todo;


public class Union2 {

    public static boolean A = false;
    public static boolean B = false;
    public static boolean C = false;

    public static void main(String[] args) throws InterruptedException {
        A = Boolean.valueOf(args[0]);
        B = Boolean.valueOf(args[1]);
        C = Boolean.valueOf(args[2]);

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

        Thread.sleep(1);

        m1();

        if(a) {
            Thread.sleep(2);
            m1();
        }

        m1();

        if(b) {
            Thread.sleep(3);
            m1();
        }

        m1();
    }

    public static void m1() throws InterruptedException {
        Thread.sleep(4);
        Thread.sleep(5);

        if(C) {
            Thread.sleep(6);
            Thread.sleep(7);
        }

        Thread.sleep(8);
        Thread.sleep(9);
    }

}