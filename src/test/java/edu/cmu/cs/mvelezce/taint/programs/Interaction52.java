package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

/*
    There seems to be a bug in the analysis of this program. Any method inside a try can throw an exception. An
    exception edge is created between the method and the caught exception instruction. An exceptional unit graph
    also adds exception edges between all predecessors and the caught instruction. Why? Nevertheless, This taints
    variables used in the sinks in the catch block.

    However, if the variables are tainted in the catch, why are those taints no carried over after the try catch?
    Why is the last if statement not tainted?

    Adding any non statement before the method that might throw an exception changes the output of the program.
 */
public class Interaction52 {

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

            Math.random();
        } catch (NullPointerException e) {
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
