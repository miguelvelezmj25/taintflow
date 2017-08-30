package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

/*

 */
public class Interaction56 {

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
                System.out.println(i);
            }

            System.out.println("");
        } catch (Exception e) {
            System.out.println();
        }

        if(x) {
            System.out.println("");
        }
    }
}
