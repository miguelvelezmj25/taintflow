package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

/*

 */
public class Interaction54 {

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

        try {
            if(a) {
                int i = 0;
                i = i % 2;
                System.out.println(i);
            }

            throw new RuntimeException();
        } catch (Exception e) {
            System.out.println();
        }

        if(a) {
            System.out.println("");
        }

        if(b) {
            System.out.println("");
        }
    }
}
