package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

/**
 * Result changes with enableExceptions. If enabled, taints are propagated. If disabled, analysis stops at try catch in throw
 */
public class Problem2 {

    public static boolean A;
    public static boolean B;

    public static void main(String[] args) {
        A = Source.getOptionA(true);
        B = Source.getOptionB(true);

        int i = 0;

        try {
            if(A) {
                i = 1;
            }

            // Tainted by A always
            Sink.getDecision(i == 0);

            throw new RuntimeException();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Tainted by A if exceptions are enabled
        Sink.getDecision(i == 0);

        if(B) {
            throw new RuntimeException();
        }

        // Tainted by A and B if exceptions are enabled
        Sink.getDecision(i == 0);
    }

}

