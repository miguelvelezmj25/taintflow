package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

public class Basic5 {

    public static void main(String[] args) throws InterruptedException {
        boolean A = Source.getOptionA();
        Basic5.foo(A);

        boolean B = A;
        Basic5.foo(B);

    }

    public static void foo(boolean x) {
        int i = 0;

        if(Sink.getDecision(x)) {
            i++;
        }
    }

}
