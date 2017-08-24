package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

/*
    Exceptions
 */
public class Interaction1 {

    public static void main(String[] args) {
        Sink.init();

        boolean A = Source.getOptionA(true);

        double d = 0D;

        if(A) {
            d = 10D;
        }

        if(d > 0) {
            System.out.println("");
        }

        int i = 0;

        if(i > 10) {
            System.out.println("");
        }

        if(d > 0) {
            throw new RuntimeException();
        }

        i = 0;

        if(i > 10) {
            System.out.println("");
        }

    }

}
