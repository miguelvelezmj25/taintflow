package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

public class Basic6 {

    public static void main(String[] args) throws InterruptedException {
        boolean A = Source.getOptionA(Boolean.valueOf(args[0]));
        int x = 0;

        if(A) {
            x++;
        }

        A = false;

        if(A) {
            x++;
        }

    }

}
