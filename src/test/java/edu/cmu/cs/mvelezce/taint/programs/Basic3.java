package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.Sink;
import edu.cmu.cs.mvelezce.analysis.Source;

public class Basic3 {

    private static Source source = new Source();
    private static Sink sink = new Sink();

    public static void main(String[] args) throws InterruptedException {
        boolean A = Basic3.source.getOptionA();

        if(sink.getDecision(A)) {
            Basic3.foo();
        }
    }

    public static void foo() {
        int x = 0;

        if(Basic3.sink.getDecision(0 > 2)) {
            x++;
        }
    }

}
