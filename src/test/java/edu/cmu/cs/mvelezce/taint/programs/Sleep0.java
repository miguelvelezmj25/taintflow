package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Sleep0 {

    public static boolean A;
    public static boolean B;

    public static void main(String[] args) throws InterruptedException {
        A = Source.getOptionA(Boolean.valueOf(args[0]));
        B = Source.getOptionB(Boolean.valueOf(args[0]));

//        boolean a = Boolean.valueOf(args[0]);
//        boolean b = Boolean.valueOf(args[1]);
//        A = Boolean.valueOf(args[0]);
//        B = Boolean.valueOf(args[1]);

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

        if(Sink.getDecision(a)) {
            Thread.sleep(2);
            Sleep0.method1(a);
        }

        Thread.sleep(3);

        if(Sink.getDecision(b)) {
            Thread.sleep(4);
            Sleep0.method2(b);
        }

    }

    public static void method1(boolean a) throws InterruptedException {
        Thread.sleep(5);

        if(Sink.getDecision(a)) {
            Thread.sleep(6);
        }

        Thread.sleep(7);
    }

    public static void method2(boolean b) throws InterruptedException {
        Thread.sleep(8);

        if(Sink.getDecision(b)) {
            Thread.sleep(9);
        }

        Thread.sleep(10);
    }

}
