package edu.cmu.cs.mvelezce.taint.programs.todo;

import edu.cmu.cs.mvelezce.analysis.option.Source;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Sleep27 {

    public static boolean A;
    public static boolean B;
    public static boolean C;

    public static void main(String[] args) throws InterruptedException {
        A = Source.getOptionA(Boolean.valueOf(args[0]));
        B = Source.getOptionB(Boolean.valueOf(args[1]));
        C = Source.getOptionC(Boolean.valueOf(args[2]));

        Thread.sleep(100);

        if(A) {
            Thread.sleep(400);
        }
        else {
            Thread.sleep(200);
        }

        if(B) {
            Thread.sleep(300);
        }
        else {
            Thread.sleep(600);
        }

        if(C) {
            Thread.sleep(800);
        }
        else {
            Thread.sleep(400);
        }


    }

}
