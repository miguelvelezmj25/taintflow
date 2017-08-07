package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

public class Basic2 {

    public static void main(String[] args) throws InterruptedException {
        boolean A = Source.getOptionA(Boolean.valueOf(args[0]));
        boolean B = false;
        int x = 0;

        if(Sink.getDecision(A)) {
            x++;
        }

        if(Sink.getDecision(B)) {
            x++;
        }

        if(Sink.getDecision(x > 0)) {
            x = -1;
        }
    }

}
