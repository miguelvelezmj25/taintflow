package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

import java.util.LinkedList;
import java.util.List;

/**
 * System libraries are not tainted.
 */
public class Problem4 {

    public static boolean A;
    public static boolean B;

    public static void main(String[] args) throws InterruptedException {
        A = Source.getOptionA(true);

        int i = 0;

        if(A) {
            i = 1;
        }

        List<Integer> x = new LinkedList<>();
        x.add(i);

        Sink.sink(x == null);
        Sink.sink(x.get(0) == 0);
    }

}

