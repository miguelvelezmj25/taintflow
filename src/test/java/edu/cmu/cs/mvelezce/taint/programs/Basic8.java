package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.Sink;
import edu.cmu.cs.mvelezce.analysis.Source;

public class Basic8 {

    private static Source source = new Source();
    private static Sink sink = new Sink();

    public static void main(String[] args) throws InterruptedException {
        boolean A = Basic8.source.getOption();
        int x = 0;

        if(Basic8.sink.getDecision(x == 0)) {
            x += foo(true);
        }

        if(Basic8.sink.getDecision(x == 1)) {
            x++;
        }

        x = foo(true);

        if(Basic8.sink.getDecision(x == 0)) {
            x = -1;
        }
    }

    public static int foo(boolean x) {
        int i = 0;

        if(Basic8.sink.getDecision(x)) {
            i++;
        }

        return i;
    }

}
