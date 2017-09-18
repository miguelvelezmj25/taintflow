package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

/*
    Multiple calls to the same method. Both sources reach the last if statement. The overapproximated result
    is to say that both sources taint the sink. However, the correct result is that the sink is tainted by each
    source separately.

    There are 3 results.
 */
public class Interaction0 {

    public static void main(String[] args) {
        Sink.init();

        boolean A = Source.getOptionA(true);

        int l = 0;


        if(A) {
            l = 10;
        }


        for(int i = 0; i < l; i++) {
            System.out.println("");
        }
    }


}
