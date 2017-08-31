package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

/*
    An exception might be thrown inside of an if statement that is tainted. This taints the variables used in the sink.

    However, if the variables are tainted in the catch, why are those taints no carried over after the try catch?
    Why is the last if statement not tainted?
 */
public class Interaction55 {

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
                Math.random();
            }

            Thread.sleep(100);
        } catch (InterruptedException e) {
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
