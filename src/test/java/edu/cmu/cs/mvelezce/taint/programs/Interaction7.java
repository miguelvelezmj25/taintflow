package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

/*
    Exceptions
 */
public class Interaction7 {

    private boolean b;

    public Interaction7(boolean b) {
        this.b = b;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }

        Interaction7 that = (Interaction7) o;

        if(this.b == that.b) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        if(this.b) {
            return 1;
        }

        return 0;
    }

    public static void main(String[] args) {
        Sink.init();

        boolean A = Source.getOptionA(true);
        boolean B = Source.getOptionB(true);

        boolean a = false;
        boolean b = false;

        if(A) {
            a = true;
        }

        if(B) {
            b = true;
        }

        Interaction7 i = new Interaction7(a);
        Interaction7 j = new Interaction7(b);

        i.hashCode();
        i.equals(j);
    }

}
