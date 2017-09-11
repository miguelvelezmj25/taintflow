package edu.cmu.cs.mvelezce.taint.programs.todo;

import edu.cmu.cs.mvelezce.analysis.option.Source;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Sleep4 {

    public static final String FILENAME = Sleep4.class.getCanonicalName();
    public static final String PACKAGE = Sleep4.class.getPackage().getName();
    public static final String CLASS = Sleep4.class.getSimpleName();
    public static final String MAIN_METHOD = "main";
    public static boolean A = false;

    public static void main(String[] args) throws InterruptedException {
        // Region program start
        System.out.println("main");

//        boolean a = SourceFormatter.getOption//        boolean a(SourceFormatter.getOption//        boolean a(Boolean.valueOf(args[0]));
        A = Source.getOptionA(Boolean.valueOf(args[0]));

        boolean a;

        if(A) {
            a = true;
        }
        else {
            a = false;
        }

        Thread.sleep(200);

        // Region A start
        if(a) { // 20
            Thread.sleep(600);
        }
        else {
            Thread.sleep(700);
        }
        // Region A end

        // Region program end
    }

}
