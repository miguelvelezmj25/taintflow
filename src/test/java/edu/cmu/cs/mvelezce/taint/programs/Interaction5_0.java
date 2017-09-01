package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

/*
    Created an edge between the throw an the catch since the catch catches the type of exception being thrown. The sink
    with variable a is tainted, but the others are not.

    There are 3 correct results.
 */
public class Interaction5_0 {

    public static void main(String[] args) {
        Sink.init();

        boolean A = Source.getOptionA(true);

        boolean a = false;
        boolean x = false;

        if(A) {
            a = true;
        }

        try {
            int i = 0;

            if(a) {
                i = i % 2;
            }

            throw new RuntimeException();
        } catch (RuntimeException e) {
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
