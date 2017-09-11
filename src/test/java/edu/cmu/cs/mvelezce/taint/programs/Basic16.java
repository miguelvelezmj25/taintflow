package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Basic16 {

    public static boolean A;
    public static boolean B;

    public static void main(String[] args) throws InterruptedException {
        A = Source.getOptionA(Boolean.valueOf(args[0]));
        B = Source.getOptionB(Boolean.valueOf(args[1]));

        int i;
        int j;

        if(A) {
            i = 1;
        }
        else {
            i = 0;
        }

        if(B) {
            j = 1;
        }
        else {
            j = 0;
        }

        int[] x = new int[2];
        int y = x[i];
        int z = x[j];

        Sink.sink(y == 0);
        Sink.sink(z == 0);
    }

}

