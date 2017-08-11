package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

import java.util.LinkedList;
import java.util.List;

/**
 * Attempted to see if the incorrect taints in systems were due to recursive calls to the entry method
 */
public class Problem5 {

    public static boolean A;
    public static boolean B;

    public static void main(String[] args) throws InterruptedException {
        A = Source.getOptionA(true);
        B = Source.getOptionB(true);

        int i = 0;
        int j = 0;

        if(A) {
            i = 1;
        }

        if(B) {
            j = 0;
        }

        Sink.getDecision(i == 0);
        Sink.getDecision(j == 0);

        if(args != null) {
            main(null);
        }
    }

}

