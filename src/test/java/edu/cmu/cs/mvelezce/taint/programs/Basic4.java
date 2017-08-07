package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

public class Basic4 {

    public static void main(String[] args) throws InterruptedException {
        boolean A = Source.getOptionA();
        Basic4.foo(A);
    }

    public static void foo(boolean x) {
        int i = 0;

        if(Sink.getDecision(x)) {
            i++;
        }
    }

}
