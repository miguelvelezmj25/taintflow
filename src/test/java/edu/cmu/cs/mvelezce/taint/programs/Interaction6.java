package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

/*
    Exceptions
 */
public class Interaction6 {

    public static void main(String[] args) {
        Sink.init();

        boolean A = Source.getOptionA(true);

        boolean a = false;

        if(A) {
            a = true;
        }

        for(int i = 0; i < 10; i++) {
            if(a) {
                throw new RuntimeException();
            }

            System.out.println("");
        }
    }

}
