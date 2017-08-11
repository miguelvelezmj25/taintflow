package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

/**
 * Check that the analysis is field sensitive
 */
public class Problem6 {

    public static boolean A;
    public static boolean B;

    public static void main(String[] args) throws InterruptedException {
        A = Source.getOptionA(true);
        B = Source.getOptionB(true);

        X x = new X(A, B);

        // Tainted by A and B
        Sink.getDecision(x == null);
        // Not tainted
        Sink.getDecision(x.some() == 0);
        // Tainted by A
        Sink.getDecision(x.a);
        // Tainted by B
        Sink.getDecision(x.b);
    }

    public static class X {
        private boolean a;
        private boolean b;

        public X(boolean a, boolean b) {
            this.a = a;
            this.b = b;
        }

        public int some() {
            return 9;
        }
    }

}

