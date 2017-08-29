package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

/*
    Multiple calls to the same method. Both sources reach the last if statement. The overapproximated result
    is to say that both sources taint the sink. However, the correct result is that the sink is tainted by each
    source separately.
 */
public class Interaction0 {

    public static void main(String[] args) {
        Sink.init();

        boolean A = Source.getOptionA(true);
        boolean B = Source.getOptionB(true);

        long l1 = 0;
        long l2 = 0;

        if(A) {
            l1 = 10;
        }

        if(B) {
            l2 = 20;
        }

        foo(l1);
        foo(l2);
    }

    public static void foo(long l) {
        if(l > 10) {
            System.out.println("");
        }
    }


}
