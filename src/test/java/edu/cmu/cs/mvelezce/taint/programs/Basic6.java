package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

public class Basic6 {

    private static Source source = new Source();
    private static Sink sink = new Sink();

    public static void main(String[] args) throws InterruptedException {
        boolean A = Basic6.source.getOptionA();
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
