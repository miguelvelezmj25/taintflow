package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

/**
 *
 */
public class Problem7 {

    public static boolean A;
    public static boolean B;

    public static void main(String[] args) {
        A = Source.getOptionA(true);
        B = Source.getOptionB(true);

        boolean a = false;
        boolean b = false;

        if(A) {
            a = true;
        }

        if(B) {
            b = true;
        }

        Sink.sink(a);
        Sink.sink(b);

        if(a) {
            if(b) {
                a = false;
            }
        }

        Sink.sink(a);
        Sink.sink(b);

    }

}

