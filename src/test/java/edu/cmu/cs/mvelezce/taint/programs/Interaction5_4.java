package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

/*
    No edge is create from the throw to the catch since the type of exception that is thrown is not caught. However,
    since an exception might be thrown inside of a tainted if, the other sinks are tainted.

    There are 3 correct results.
 */
public class Interaction5_4 {

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
                throw new RuntimeException();
            }

        } catch (ArithmeticException e) {
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
