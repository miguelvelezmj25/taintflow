package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

import java.util.ArrayList;
import java.util.List;

/**
 * System libraries are not tainteds
 */
public class Problem10 {

    public static void main(String[] args) {
        boolean b = Source.getOptionA(true);

        List<A> as = new ArrayList<>();
        as.add(new A());

        for(A a : as) {
            if(Sink.getDecision(!a.isB())) {
                a.setB(b);
            }
        }

        for(A a : as) {
            if(Sink.getDecision(a.isB())) {
                System.out.println("");
            }
        }
    }

    private static class A {
        boolean b;

        public A() {
            this.b = false;
        }

        public boolean isB() {
            return b;
        }

        public void setB(boolean b) {
            this.b = b;
        }
    }
}

