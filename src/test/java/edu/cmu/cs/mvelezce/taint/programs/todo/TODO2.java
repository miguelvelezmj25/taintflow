package edu.cmu.cs.mvelezce.taint.programs.todo;

import edu.cmu.cs.mvelezce.analysis.option.Source;
import edu.cmu.cs.mvelezce.taint.programs.Basic2;

/**
 * Created by mvelezce on 4/21/17.
 */
public class TODO2 {

    public static final String FILENAME = Basic2.class.getCanonicalName();
    public static final String PACKAGE = Basic2.class.getPackage().getName();
    public static final String CLASS = Basic2.class.getSimpleName();
    public static final String MAIN_METHOD = "main";
    public static final String METHOD_1 = "method1";
    public static boolean A = false;

    public static void main(String[] args) throws InterruptedException {
        // Region program start
        System.out.println("main");

//        boolean a = SourceFormatter.getOption//        boolean a(Boolean.valueOf(args[0]));
        A = Source.getOptionA(Boolean.valueOf(args[0]));

        Thread.sleep(200);

        // Region A start
        if(A) { // 20
            Thread.sleep(600);
            TODO2.method1(A);
        }
        // Region A end
        Thread.sleep(100);
        TODO2.method1(false);
        Thread.sleep(150);

        // Region program end
    }

    public static void method1(boolean A) throws InterruptedException {
        System.out.println("method12");
        boolean a = A;
        Thread.sleep(200);

        // Region A start
        if(a) { // 16
            Thread.sleep(600);
        }
        // Region A end

        Thread.sleep(100);
    }

}
