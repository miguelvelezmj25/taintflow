package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

/**
 * Created by mvelezce on 6/16/17.
 */
public class Basic9 {

    private static boolean A;

    public static void main(String[] args) {
        A = Source.getOptionA(Boolean.valueOf(args[0]));

        int a = A ? 1 : 0;

        if(Sink.getDecision(a == 0)) {
            System.out.println("foo");
        }

//        new X(a);
//        new Y().foo(a);
        new W(a);
        new Z().foo(a);
    }


}

class W {
    public W(int a) {

        if(Sink.getDecision(a == 0)) {
            System.out.println("foo");
        }
    }
}

class Z {
    public void foo(int a) {
        if(Sink.getDecision(a == 0)) {
            System.out.println("foo");
        }

    }
}
