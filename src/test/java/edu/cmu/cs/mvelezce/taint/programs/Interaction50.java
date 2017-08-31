package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

/*
    Try catch with a control flow decision that uses a tainted value. Try catch is present in jimple, but, since the
    exception is thrown outside of a condition, it does not taint the catch block.

    There are 3 correct results.
 */
public class Interaction50 {

    public static void main(String[] args) {
        Sink.init();

        boolean A = Source.getOptionA(true);

        boolean a = false;
        boolean x = false;

        if(A) {
            a = true;
        }

        try {
            if(a) {
                int i = 0;
                i = i % 2;
            }

            throw new InterruptedException();
        } catch (Exception e) {
            Sink.sink(a);
            Sink.sink(x);
            Sink.sink(e);
            Math.random();
        }

        if(x) {
            Math.random();
        }
    }
}
