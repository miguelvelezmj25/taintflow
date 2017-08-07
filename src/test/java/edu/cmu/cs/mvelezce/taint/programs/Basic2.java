package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

public class Basic2 {

    public static void main(String[] args) throws InterruptedException {
        Source source = new Source();
        Sink sink = new Sink();

        boolean A = source.getOptionA();
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
