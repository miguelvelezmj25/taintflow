package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

/**
 * System libraries are not tainteds
 */
public class Problem11 {

    public static void main(String[] args) {
        boolean A = Source.getOptionA(true);
        boolean B = Source.getOptionB(true);

        boolean b = false;
        int i = 0;

        if(A) {
            b = true;
        }

        if(B) {
            i = 1;
        }


        A a = new A();
        a.getB().setB(b);
        a.getB().setI(i);

        Sink.getDecision(a.getB().isB());
        Sink.getDecision(a.getB().getI() == 0);
    }

    private static class A {
        B b;

        public A() {
            this.b = new B();
        }

        public B getB() {
            return b;
        }
    }

    private static class B {
        boolean b;
        int i;

        public boolean isB() {
            return b;
        }

        public void setB(boolean b) {
            this.b = b;
        }

        public int getI() {
            return i;
        }

        public void setI(int i) {
            this.i = i;
        }
    }
}

