package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

/**
 * Everything is tainted after control flow decision that depends on tainted value and the body throws exception
 */
public class Problem1 {

    public static boolean A;

    public static void main(String[] args) {
        A = Source.getOptionA(true);

        int a = 0;
        int b = 0;

        if(A) {
            throw new RuntimeException();
        }

        // Tainted by A
        Sink.sink(a == 0);
        // Tainted by A
        Sink.sink(b == 0);
    }

}

