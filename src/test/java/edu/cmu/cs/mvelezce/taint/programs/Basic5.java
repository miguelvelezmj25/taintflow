package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.Sink;
import edu.cmu.cs.mvelezce.analysis.Source;

public class Basic5 {

    private static Source source = new Source();
    private static Sink sink = new Sink();

    public static void main(String[] args) throws InterruptedException {
        boolean A = Basic5.source.getOption();
        Basic5.foo(A);

        boolean B = A;
        Basic5.foo(B);

    }

    public static void foo(boolean x) {
        int i = 0;

        if(Basic5.sink.getDecision(x)) {
            i++;
        }
    }

}
