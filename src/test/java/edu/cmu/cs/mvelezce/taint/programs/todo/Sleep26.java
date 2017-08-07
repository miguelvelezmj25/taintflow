package edu.cmu.cs.mvelezce.taint.programs.todo;

import edu.cmu.cs.mvelezce.analysis.option.Source;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Sleep26 {

    public static boolean A = false;

    public static void main(String[] args) throws InterruptedException {
        A = Source.getOptionA(Boolean.valueOf(args[0]));

        Thread.sleep(200);
        Sleep26.method1(true);
        Thread.sleep(200);
        Sleep26.method1(false);
        Thread.sleep(100);

        if(A) {
            Thread.sleep(200);
        }
    }

    public static void method1(boolean a) throws InterruptedException {
        Thread.sleep(200);

        // Region A start
        if(a || A) { // 16
            Thread.sleep(600);
        }
        // Region A end

        Thread.sleep(100);
    }

}
