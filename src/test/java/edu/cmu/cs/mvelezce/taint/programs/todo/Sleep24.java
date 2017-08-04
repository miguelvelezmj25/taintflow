package edu.cmu.cs.mvelezce.taint.programs.todo;

/**
 * Created by mvelezce on 7/13/17.
 */
public class Sleep24 {
    public static boolean A = false;
    public static boolean B = false;

    public static void main(String[] args) throws InterruptedException {
        A = Boolean.valueOf(args[0]);
        B = Boolean.valueOf(args[1]);

        if(A) {
            m1(3);
        }
        else {
            m1(5);
        }

        if(B) {
            m1(2);
        }
        else {
            m1(4);
        }

    }

    public static void m1(int i) throws InterruptedException {
        Thread.sleep(200 * i);
        Thread.sleep(300 * i);
    }
}
