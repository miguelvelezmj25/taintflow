package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.Options;

public class Basic1 {

    public static void main(String[] args) throws InterruptedException {
        Options options = new Options();
        boolean A = options.getOption();
        int x = 0;

        if(options.getDecision(A)) {
            x = 1;
        }

        if(options.getDecision(x == 0)) {
            x = -1;
        }

    }

}
