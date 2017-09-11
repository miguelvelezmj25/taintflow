package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

/*
    Explicitly throwing an exception inside of a try. Since the if statement inside of the try does not have a tainted
    variable, the conditions after the catch block behave as excepted.

    There are 2 results
 */
public class Interaction4 {

    public static void main(String[] args) {
        Sink.init();

        boolean A = Source.getOptionA(true);

        boolean a = false;
        boolean b = false;

        if(A) {
            a = true;
        }

        try {
            if(b) {
                System.out.println("");
            }

            throw new RuntimeException();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(a) {
            System.out.println("");
        }

        if(b) {
            System.out.println("");
        }
    }

}
