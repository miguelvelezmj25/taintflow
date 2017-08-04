package edu.cmu.cs.mvelezce.taint.programs.todo;


public class Union3 {

    public static boolean A = false;
    public static boolean B = false;

    public static void main(String[] args) throws InterruptedException {
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

        Thread.sleep(1);

//        m1(true);

        if(a) {
            Thread.sleep(2);
            m1(a);
        }

        m1(false);

        if(b) {
            Thread.sleep(3);
            m1(b);
        }

//        m1();
    }

    public static void m1(boolean x) throws InterruptedException {
        Thread.sleep(4);
        Thread.sleep(5);
        Thread.sleep(6);
        Thread.sleep(7);
    }

}

