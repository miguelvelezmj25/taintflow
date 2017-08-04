package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.Options;

public class Basic6 {

    private static Options options = new Options();

    public static void main(String[] args) throws InterruptedException {
        boolean A = Basic6.options.getOption();
        int x = 0;

        if(Basic6.options.getDecision(A)) {
            x++;
        }

        A = false;

        if(Basic6.options.getDecision(A)) {
            x++;
        }

    }

}
