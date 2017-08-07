package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

public class Basic4 {

    private static Source source = new Source();
    private static Sink sink = new Sink();

    public static void main(String[] args) throws InterruptedException {
        boolean A = Basic4.source.getOptionA();
        Basic4.foo(A);
    }

    public static void foo(boolean x) {
        int i = 0;

        if(Basic4.sink.getDecision(x)) {
            i++;
        }
    }

}
