package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

/*
    When an if statement follows directly a try block and the first instruction of the try block is a
    sink, the sink is tainted. There seems to be a bug with the analysis. I added a nop before each
    sink to fix this issue
 */
public class Interaction2 {

    public static void main(String[] args) {
        Sink.init();

        boolean A = Source.getOptionA(true);

        boolean a = false;
        boolean b = false;

        if(A) {
            a = true;
        }

        try {
            if(b) {
                System.out.println("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(a) {
            System.out.println("");
        }
    }

}
