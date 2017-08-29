package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

/*
    Exception
 */
public class Interaction12 {

    public static void main(String[] args) {
        Sink.init();

        boolean A = Source.getOptionA(true);
        boolean b = false;
        System.out.println("start");

        try {
            int i = 0;
            if(A) {
                i = 1;
//                Thread.sleep(45);
            }
            throw new InterruptedException();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.println("end");

        if(b) {
            System.out.println("");
        }

    }

}
