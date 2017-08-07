package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

public class Basic0 {

    public static void main(String[] args) throws InterruptedException {
        Source source = new Source();
        Sink sink = new Sink();

        boolean A = source.getOptionA();

        sink.getDecision(true);
        sink.getDecision(false);
    }

}
