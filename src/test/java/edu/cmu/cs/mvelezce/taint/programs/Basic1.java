package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

public class Basic1 {

    public static void main(String[] args) throws InterruptedException {
        boolean A = Source.getOptionA();
        int x = 0;

        if(Sink.getDecision(x == 0)) {
            x++;
        }

        if(Sink.getDecision(A)) {
            x++;
        }

        if(Sink.getDecision(x == 0)) {
            x++;
        }

    }

}
