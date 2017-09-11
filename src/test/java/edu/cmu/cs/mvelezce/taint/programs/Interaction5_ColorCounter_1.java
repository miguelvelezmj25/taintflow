package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

/*
    Interaction occurs in the last decision since an exception might be thrown depending on a condition. Therefore, all
    following decision depend on that condition.
 */
public class Interaction5_ColorCounter_1 {

    public static void main(String[] args) {
        Sink.init();

        boolean A = Source.getOptionA(true);
        boolean B = Source.getOptionB(true);

        if(A) {
            throw new RuntimeException();
        }

        foo(B);

    }

    private static void foo(boolean b) {
        if(b) {
            System.out.println("SD");
        }
    }

}
