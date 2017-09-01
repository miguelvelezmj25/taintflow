package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

/*
    When an exception is thrown depending on an option, all following control flow decisions depend on that
    option. This is the correct result.

    There are no exception edges in this example

    There are 5 results
 */
public class Interaction1 {

    public static void main(String[] args) {
        Sink.init();

        boolean A = Source.getOptionA(true);
        boolean B = Source.getOptionB(true);

        boolean a = false;
        boolean b = false;

        if(A) {
            a = true;
        }

        if(B) {
            b = true;
        }

        boolean x = false;

        if(x) {
            System.out.println("");
        }

        if(a) {
            throw new RuntimeException();
        }

        if(x) {
            System.out.println("");
        }

        if(b) {
            System.out.println("");
        }

    }

}
