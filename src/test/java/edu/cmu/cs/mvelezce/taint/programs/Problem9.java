package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

/**
 * PLDI example
 */
public class Problem9 {

    public static void main(String[] args) {
        A a = new A();
        B b = a.g;
        foo(a);
        Sink.getDecision(b.f);
    }

    private static void foo(A z) {
        B x = z.g;
        boolean w = Source.getOptionA(true);
        x.f = w;
    }

    private static class A {
        B g;
    }

    private static class B {
        boolean f;
    }
}

