package edu.cmu.cs.mvelezce.taint.programs.todo;

import edu.cmu.cs.mvelezce.analysis.option.Source;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Sleep16 {

    public static final String FILENAME = Sleep16.class.getCanonicalName();
    public static final String PACKAGE = Sleep16.class.getPackage().getName();
    public static final String CLASS = Sleep16.class.getSimpleName();
    public static final String MAIN_METHOD = "main";
    public static final String METHOD_1 = "method1";
    public static boolean A = false;
    public static boolean B = false;

    public static void main(String[] args) throws InterruptedException {
        // Region program start
        System.out.println("main");

//        boolean a = Source.getOption//        boolean a(Boolean.valueOf(args[0]));
//        boolean b = Source.getOption//        boolean b(Boolean.valueOf(args[1]));
        A = Source.getOptionA(Boolean.valueOf(args[0]));

        boolean a;
        boolean b;

        if(A) {
            a = true;
        }
        else {
            a = false;
        }

        B = Source.getOptionB(Boolean.valueOf(args[1]));

        if(B) {
            b = true;
        }
        else {
            b = false;
        }

        Thread.sleep(200);

        // Region A start
        if(a) { // 20
            Thread.sleep(600);
            Sleep16.method1(a, b);
        }
        // Region A end
        Thread.sleep(100);
//        Sleep16.method1(true, true);
        Thread.sleep(150);

        // Region program end
    }

    public static void method1(boolean a, boolean b) throws InterruptedException {
        System.out.println("method12");
        Thread.sleep(200);

        if(a || b) {
            Thread.sleep(600);
        }

        if(a && b) {
            Thread.sleep(600);
        }

        Thread.sleep(100);
    }

}
