package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

import java.util.ArrayList;
import java.util.List;

/*
    Object reference is tainted with all vairables that tainted its fields
 */
public class Interaction9 {

    private boolean b;
    private boolean c;

    public Interaction9(boolean b, boolean c) {
        this.b = b;
        this.c = c;
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

        Interaction9 i = new Interaction9(a, b);

        if(i == null) {
            System.out.println();
        }
    }

}
