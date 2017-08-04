package edu.cmu.cs.mvelezce.taint.programs.todo;

/**
 * Created by mvelezce on 6/16/17.
 */
public class TODO3 {

    public static boolean A = false;
    public static boolean B = false;
    public static boolean C = false;
    public static int IA = 0;

    public static void main(String[] args) throws InterruptedException {
        int i = 0;

        if(A) {
            Thread.sleep(100);
            i = 1;
        }

        if(B && i > 0) {
            Thread.sleep(200);
        }

        if(C) {
            Thread.sleep(300);
        }
    }
}

