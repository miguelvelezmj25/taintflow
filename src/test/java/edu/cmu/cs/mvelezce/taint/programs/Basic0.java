package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

import java.util.ArrayList;
import java.util.List;

public class Basic0 {

    public static void main(String[] args) throws InterruptedException {
        boolean a = Source.getOptionA(Boolean.valueOf(args[0]));

        if(a) {
            System.out.println();
        }

        List<Boolean> x = new ArrayList<>();
        x.add(a);

        if(x.get(0)) {
            System.out.println("");
        }

        Sink.sink(true);
        Sink.sink(false);
    }

}
