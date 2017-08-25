package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

import java.util.ArrayList;
import java.util.List;

/*
    Equals and hashcode
 */
public class Interaction8 {

    private boolean b;

    public Interaction8(boolean b) {
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

        Interaction8 that = (Interaction8) o;

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

        boolean a = false;

        if(A) {
            a = true;
        }

        Interaction8 i = new Interaction8(a);

        List<Interaction8> interactions = new ArrayList<>();

        if(!interactions.contains(i)) {
            interactions.add(i);
        }

        if(!interactions.contains(i)) {
            interactions.add(i);
        }
    }

}
