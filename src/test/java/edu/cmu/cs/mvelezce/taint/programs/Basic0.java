package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

public class Basic0 {

    public static void main(String[] args) throws InterruptedException {
        boolean A = Source.getOptionA();

        Sink.getDecision(true);
        Sink.getDecision(false);
    }

}
