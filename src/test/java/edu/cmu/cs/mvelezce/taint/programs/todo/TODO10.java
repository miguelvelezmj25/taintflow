package edu.cmu.cs.mvelezce.taint.programs.todo;

import edu.cmu.cs.mvelezce.analysis.option.Source;

/**
 * Created by mvelezce on 7/8/17.
 */
public class TODO10 {

    public static boolean A = false;
    public static boolean B = false;

    public static void main(String[] args) {
        A = Source.getOptionA(Boolean.valueOf(args[0]));
        B = Source.getOptionB(Boolean.valueOf(args[1]));

        A = foo(A);
        B = foo(B);

        if(A) {
            System.out.println(0);
        }

        if(B) {
            System.out.println(1);
        }


    }

    static boolean foo(boolean x) {
        return x;
    }
}
