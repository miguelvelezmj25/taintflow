package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

/*
    Equals and hashcode are called explicitly and are tainted inside
 */
public class Interaction8_0 {

    private boolean b;

    public Interaction8_0(boolean b) {
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

        Interaction8_0 that = (Interaction8_0) o;

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

        Interaction8_0 i = new Interaction8_0(a);
        Interaction8_0 j = new Interaction8_0(b);

        i.hashCode();
        i.equals(j);
    }

}
