package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

public class Interaction01 {

    public static void main(String[] args) {
        Sink.init();

        boolean A = Source.getOptionA(true);
        boolean a = false;

        if(A) {
            a = true;
        }

        System.out.println(a);
    }

}
