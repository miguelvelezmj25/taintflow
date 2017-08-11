package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

/**
 * Result changes with enableExceptions and in methods. If enabled, taints are propagated correctly. If disabled, taints are propagated incorrectly
 */
public class Problem3 {

    public static boolean A;
    public static boolean B;

    public static void main(String[] args) {
        A = Source.getOptionA(true);
        B = Source.getOptionB(true);

        moo(A, B);
//        moo(A);
    }

    private static void moo(boolean a, boolean b) {
        int i = 0;

        try {
            if(a) {
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

        if(b) {
            throw new RuntimeException();
        }

        // Tainted by B if exceptions are not enabled. Tainted by A and B if exceptions are enabled
        Sink.getDecision(i == 0);
    }

}

