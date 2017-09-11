package edu.cmu.cs.mvelezce.taint.programs;


import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

public class Union0 {

    public static boolean A;
    public static boolean B;

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

        Thread.sleep(1);

        if(a) {
            Thread.sleep(2);
            m1();
        }

        if(b) {
            Thread.sleep(3);
            m1();
        }
    }

    public static void m1() throws InterruptedException {
        Thread.sleep(4);
        Thread.sleep(5);
        Thread.sleep(6);
        Thread.sleep(7);
    }

}