package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

public class Basic3 {

    public static void main(String[] args) throws InterruptedException {
        boolean A = Source.getOptionA(Boolean.valueOf(args[0]));

        if(A) {
            Basic3.foo();
        }
    }

    public static void foo() {
        int x = 0;

        if(0 > 2) {
            x++;
        }
    }

}
