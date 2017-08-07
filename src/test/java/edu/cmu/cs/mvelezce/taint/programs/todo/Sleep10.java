package edu.cmu.cs.mvelezce.taint.programs.todo;

import edu.cmu.cs.mvelezce.analysis.option.Source;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Sleep10 {

    public static final String FILENAME = Sleep10.class.getCanonicalName();
    public static final String PACKAGE = Sleep10.class.getPackage().getName();
    public static final String CLASS = Sleep10.class.getSimpleName();
    public static final String MAIN_METHOD = "main";
    public static final String METHOD_1 = "method1";
    public static final String METHOD_2 = "method2";
    public static boolean A = false;
    public static boolean B = false;

    public static void main(String[] args) throws InterruptedException {
        // Region program start
        System.out.println("main");

//        boolean a = Source.getOption//        boolean a(Boolean.valueOf(args[0]));
//        boolean b = Source.getOption//        boolean b(Boolean.valueOf(args[1]));
        A = Source.getOptionA(Boolean.valueOf(args[0]));
        B = Source.getOptionB(Boolean.valueOf(args[1]));

        boolean a;

        if(A) {
            a = true;
        }
        else {
            a = false;
        }

        boolean b;

        if(B) {
            b = true;
        }
        else {
            b = false;
        }

        Thread.sleep(100);
        if(a) { // 28
            // Region A start
            Thread.sleep(200);
            Sleep10.method1(a);
            // Region A end
        }
        else if(b) { // 42
            // Region B start
            Thread.sleep(300);
            Sleep10.method2(b);
            // Region B end
        }

        Thread.sleep(400);
        // Region program end
    }

    public static void method1(boolean A) throws InterruptedException {
        System.out.println("method1");
        boolean a = A;
        Thread.sleep(500);

        // Region A start
        if(a) { // 16
            Thread.sleep(600);
        }
        // Region A end 20

        Thread.sleep(700);
    }

    public static void method2(boolean B) throws InterruptedException {
        System.out.println("method2");
        boolean b = B;
        Thread.sleep(800);

        // Region B start
        if(b) { // 16
            Thread.sleep(900);
        }
        // Region B end

        Thread.sleep(1000);
    }

}
