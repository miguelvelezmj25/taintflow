package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

/*
    Exception
 */
public class Interaction10 {

    public static void main(String[] args) {
        Sink.init();

        boolean A = Source.getOptionA(true);

        boolean a = false;

        if(A) {
            a = true;
        }

        String s = "";

        try {
            s = s.substring(-10, -5);
        }
        catch(Exception e) {
            System.out.println();
        }

        int i = 0;

        if(i / 2 == 2) {
            System.out.println();
        }
    }

}
