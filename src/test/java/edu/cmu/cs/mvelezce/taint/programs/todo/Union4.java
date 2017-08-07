package edu.cmu.cs.mvelezce.taint.programs.todo;

import edu.cmu.cs.mvelezce.analysis.option.Source;

public class Union4 {

    public static boolean A = false;
    public static boolean B = false;
    public static boolean C = false;

    public static void main(String[] args) throws InterruptedException {
        A = Source.getOptionA(Boolean.valueOf(args[0]));
        B = Source.getOptionB(Boolean.valueOf(args[1]));
        C = Source.getOptionC(Boolean.valueOf(args[2]));

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

        if(a) {
            Thread.sleep(2);
            m1(a);
        }

        m1(true);
        m1(false);

        if(b) {
            Thread.sleep(3);
            m1(b);
        }

//        m1();
    }

    public static void m1(boolean x) throws InterruptedException {
        Thread.sleep(5);
        Thread.sleep(5);

        if(C) {
            Thread.sleep(6);
            Thread.sleep(7);
        }

        Thread.sleep(8);
        Thread.sleep(9);
    }

}
