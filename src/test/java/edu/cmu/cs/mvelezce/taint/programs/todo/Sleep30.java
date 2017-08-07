package edu.cmu.cs.mvelezce.taint.programs.todo;

import edu.cmu.cs.mvelezce.analysis.option.Source;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Sleep30 {

    public static boolean A = false;
    public static boolean B = false;

    public static void main(String[] args) throws InterruptedException {
        A = Source.getOptionA(Boolean.valueOf(args[0]));
        B = Source.getOptionB(Boolean.valueOf(args[1]));

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
            Thread.sleep(100);
            m1();
        }

        if(b) {
            Thread.sleep(300);
            m1();
        }

        m1();
    }

    public static void m1() throws InterruptedException {
        Thread.sleep(500);
    }

}
