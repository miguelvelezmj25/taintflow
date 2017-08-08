package edu.cmu.cs.mvelezce.taint.programs;


import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

public class Union2 {

    public static boolean A;
    public static boolean B;
    public static boolean C;

    public static void main(String[] args) throws InterruptedException {
        A = Source.getOptionA(Boolean.valueOf(args[0]));
        B = Source.getOptionB(Boolean.valueOf(args[1]));
        C = Source.getOptionC(Boolean.valueOf(args[2]));

        boolean a;
        boolean b;

        if(Sink.getDecision(A)) {
            a = true;
        }
        else {
            a = false;
        }

        if(Sink.getDecision(B)) {
            b = true;
        }
        else {
            b = false;
        }

        Thread.sleep(1);

        m1();

        if(Sink.getDecision(a)) {
            Thread.sleep(2);
            m1();
        }

        m1();

        if(Sink.getDecision(b)) {
            Thread.sleep(3);
            m1();
        }

        m1();
    }

    public static void m1() throws InterruptedException {
        Thread.sleep(4);
        Thread.sleep(5);

        if(Sink.getDecision(C)) {
            Thread.sleep(6);
            Thread.sleep(7);
        }

        Thread.sleep(8);
        Thread.sleep(9);
    }

}
