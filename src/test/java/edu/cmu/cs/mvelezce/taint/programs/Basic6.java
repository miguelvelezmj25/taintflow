package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.Sink;
import edu.cmu.cs.mvelezce.analysis.Source;

public class Basic6 {

    private static Source source = new Source();
    private static Sink sink = new Sink();

    public static void main(String[] args) throws InterruptedException {
        boolean A = Basic6.source.getOption();
        int x = 0;

        if(Basic6.sink.getDecision(A)) {
            x++;
        }

        A = false;

        if(Basic6.sink.getDecision(A)) {
            x++;
        }

    }

}
