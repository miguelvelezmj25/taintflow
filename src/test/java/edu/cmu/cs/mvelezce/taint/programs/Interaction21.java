package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

/*
    Exceptions
 */
public class Interaction21 {

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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(a) {
            System.out.println("");
        }
    }

}
