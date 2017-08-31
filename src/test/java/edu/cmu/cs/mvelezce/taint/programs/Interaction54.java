package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

/*
    An exception might be thrown inside of an if statement that is tainted. This taints the variables used in the sink.
    Since an exception is thrown in the try block, the catch block is always executed which leads to the if statement
    after the try catch block to be tainted. This might explain the reason why in other interactions 5X the last if
    statement is not tainted; in those cases, there was always another path that did not go through the catch block and
    this might take precedence on how the join is performed between a regular edge and an edge coming from a catch block
 */
public class Interaction54 {

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
