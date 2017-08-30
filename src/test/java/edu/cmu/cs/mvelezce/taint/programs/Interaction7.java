package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

/*
    Loops
 */
public class Interaction7 {

    public static void main(String[] args) {
        Sink.init();

        boolean A = Source.getOptionA(true);

        boolean a = false;

        if(A) {
            a = true;
        }

        for(int i = 0; i < 10; i++) {
            if(a) {
                System.out.println("");
            }

            System.out.println("");
        }
    }

}
