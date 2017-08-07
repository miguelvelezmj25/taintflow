package edu.cmu.cs.mvelezce.taint.programs.todo;

import edu.cmu.cs.mvelezce.analysis.option.Source;

/**
 * Created by mvelezce on 7/13/17.
 */
public class Sleep23 {
    public static boolean A = false;
    public static boolean B = false;

    public static void main(String[] args) throws InterruptedException {
        A = Source.getOptionA(Boolean.valueOf(args[0]));
        B = Source.getOptionB(Boolean.valueOf(args[1]));

        Thread.sleep(200);

        if(A) {
            Thread.sleep(200);
        }
        else {
            Thread.sleep(300);
        }

        Thread.sleep(100);

        if(B) {
            Thread.sleep(600);
        }
        else {
            Thread.sleep(400);
        }

    }
}
