package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

public class Basic7 {

    private static Source source = new Source();
    private static Sink sink = new Sink();

    public static void main(String[] args) throws InterruptedException {
        boolean A = Basic7.source.getOptionA();
        int x = 0;

        if(Basic7.sink.getDecision(A)) {
            x += foo(true);
        }

        if(Basic7.sink.getDecision(x == 1)) {
            x++;
        }

        x = foo(true);

        if(Basic7.sink.getDecision(x == 0)) {
            x = -1;
        }
    }

    public static int foo(boolean x) {
        int i = 0;

        if(Basic7.sink.getDecision(x)) {
            i++;
        }

        return i;
    }

}
