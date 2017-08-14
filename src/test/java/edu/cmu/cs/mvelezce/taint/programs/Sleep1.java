package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Sleep1 {

    public static boolean A;
    public static boolean B;

    public static void main(String[] args) {
        A = Source.getOptionA(Boolean.valueOf(args[0]));
        B = Source.getOptionB(Boolean.valueOf(args[0]));

        Integer a;
        Integer b;

        if(Sink.getDecision(A)) {
            a = 0;
        }
        else {
            a = 5;
        }

        if(Sink.getDecision(B)) {
            b = 0;
        }
        else {
            b = 10;
        }

        if(Sink.getDecision(a > 10)) {
            Sleep1.method1(a);
        }

        if(Sink.getDecision(b > 10)) {
            Sleep1.method2(b);
        }

    }

    public static void method1(int a) {
        if(Sink.getDecision(a > 0)) {
            System.out.println("");
        }


    }

    public static void method2(int b) {
        if(Sink.getDecision(b > 0)) {
            System.out.println("");
        }

    }

}
