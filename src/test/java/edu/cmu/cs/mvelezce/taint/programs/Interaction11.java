package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

/*
    If in constructor
 */
public class Interaction11 {

    private int i;

    public Interaction11(int i) {
        if(i == 0) {
            this.i = -1;
        }
        else {
            this.i = 100;
        }
    }

    public static void main(String[] args) {
        Sink.init();

        boolean A = Source.getOptionA(true);

        boolean a = false;

        if(A) {
            a = true;
        }

        int i = 0;

        if(a) {
            new Interaction11(i);
        }


    }

}
