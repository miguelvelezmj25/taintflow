package edu.cmu.cs.mvelezce.taint.programs.todo;

import edu.cmu.cs.mvelezce.taint.programs.Sleep0;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Sleep18 {

    public static final String FILENAME = Sleep0.class.getCanonicalName();
    public static final String PACKAGE = Sleep0.class.getPackage().getName();
    public static final String CLASS = Sleep0.class.getSimpleName();
    public static final String MAIN_METHOD = "main";
    public static boolean A = false;
    public static boolean B = false;
    public static boolean C = false;

    public static void main(String[] args) throws InterruptedException {
        A = Boolean.valueOf(args[0]);
        B = Boolean.valueOf(args[1]);
        C = Boolean.valueOf(args[2]);

        Thread.sleep(100);

        if(A) {
            Thread.sleep(200);
            if(B) {
                Thread.sleep(300);
                if(C) {
                    Thread.sleep(400);
                }
                else {
                    Thread.sleep(500);
                }
            }
        }

        Thread.sleep(600);
    }
}
