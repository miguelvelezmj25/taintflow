package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Basic18 {

    public static boolean A;
    public static boolean B;

    public static void main(String[] args) throws InterruptedException {
        A = Source.getOptionA(Boolean.valueOf(args[0]));
        B = Source.getOptionB(Boolean.valueOf(args[1]));

        int i;
        int j;

        if(A) {
            i = 1;
        }
        else {
            i = 0;
        }

        if(B) {
            j = 1;
        }
        else {
            j = 0;
        }

        List<Integer> x = new LinkedList<>();
        x.add(i);
        x.add(j);

        Sink.getDecision(x == null);
        Sink.getDecision(x.get(0) == 0);
        Sink.getDecision(x.get(1) == 0);
    }

}

