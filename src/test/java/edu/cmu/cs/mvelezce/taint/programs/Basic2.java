package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.Sink;
import edu.cmu.cs.mvelezce.analysis.Source;

public class Basic2 {

    public static void main(String[] args) throws InterruptedException {
        Source source = new Source();
        Sink sink = new Sink();

        boolean A = source.getOption();
        boolean B = false;
        int x = 0;

        if(sink.getDecision(A)) {
            x++;
        }

        if(sink.getDecision(B)) {
            x++;
        }

        if(sink.getDecision(x > 0)) {
            x = -1;
        }
    }

}
