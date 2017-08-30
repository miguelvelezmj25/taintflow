package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

/*
    Try catch with a control flow decision that uses a tainted value. Try catch is present in jimple, but that does not
    taint the control flow decisions after the catch block. There are 4 exceptional edges that point to the catching of
    an exception:

    - nop
    - sink
    - $r0 = new java.lang.RuntimeException
    - specialinvoke $r0.<java.lang.RuntimeException: void <init>()>()

    There are 5 correct results.
 */
public class Interaction50 {

    public static void main(String[] args) {
        Sink.init();

        boolean A = Source.getOptionA(true);

        boolean a = false;
        boolean x = false;

        if(A) {
            a = true;
        }

        try {
            if(a) {
                int i = 0;
                i = i % 2;
            }

            throw new RuntimeException();
        } catch (Exception e) {
            System.out.println();
        }

        if(x) {
            System.out.println("");
        }
    }
}
