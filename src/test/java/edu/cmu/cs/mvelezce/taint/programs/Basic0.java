package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

public class Basic0 {

    public static void main(String[] args) throws InterruptedException {
        boolean a = Source.getOptionA(Boolean.valueOf(args[0]));

        if(Sink.getDecision(a)) {
//        if(a) {
            System.out.println("");
        }
    }

}
