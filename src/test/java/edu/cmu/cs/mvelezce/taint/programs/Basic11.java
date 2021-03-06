package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Basic11 {

    public static boolean A;
    public static boolean B;

    public static void main(String[] args) throws InterruptedException {
        A = Source.getOptionA(Boolean.valueOf(args[0]));
        B = Source.getOptionB(Boolean.valueOf(args[0]));

        Integer a;
        Integer b;

        if(A) {
            a = 0;
        }
        else {
            a = 5;
        }

        if(B) {
            b = 0;
        }
        else {
            b = 10;
        }

        if(a > 10) {
            throw new RuntimeException();
        }

        if(b > 10) {
            System.out.println(";");
        }

    }

}
