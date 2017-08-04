package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.Options;

public class Basic7 {

    private static Options options = new Options();

    public static void main(String[] args) throws InterruptedException {
        boolean A = Basic7.options.getOption();
        int x = 0;

        if(Basic7.options.getDecision(A)) {
            x += foo(true);
        }

        if(Basic7.options.getDecision(x == 1)) {
            x++;
        }

        x = foo(true);

        if(Basic7.options.getDecision(x == 0)) {
            x = -1;
        }

    }

    public static int foo(boolean x) {
        int i = 0;

        if(Basic7.options.getDecision(x)) {
            i++;
        }

        return i;
    }

}
