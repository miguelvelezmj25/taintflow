package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

public class Basic8 {

    public static void main(String[] args) throws InterruptedException {
        boolean A = Source.getOptionA(Boolean.valueOf(args[0]));
        int x = 0;

        if(x == 0) {
            x += foo(true);
        }

        if(x == 1) {
            x++;
        }

        x = foo(true);

        if(x == 0) {
            x = -1;
        }
    }

    public static int foo(boolean x) {
        int i = 0;

        if(x) {
            i++;
        }

        return i;
    }

}
