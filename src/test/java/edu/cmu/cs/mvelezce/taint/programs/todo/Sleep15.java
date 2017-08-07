package edu.cmu.cs.mvelezce.taint.programs.todo;

import edu.cmu.cs.mvelezce.analysis.option.Source;

/**
 * Created by mvelezce on 6/16/17.
 */
public class Sleep15 {

    public static final String FILENAME = Sleep15.class.getCanonicalName();
    public static final String PACKAGE = Sleep15.class.getPackage().getName();
    public static final String CLASS = Sleep15.class.getSimpleName();
    public static final String MAIN_METHOD = "main";
    public static boolean A = false;
    public static boolean B = false;
    public static boolean C = false;

    public static void main(String[] args) throws InterruptedException {
//        System.out.println("main");
//
////        boolean a = Source.getOption////        boolean a(Boolean.valueOf(args[0]));
////        boolean b = Source.getOption////        boolean b(Boolean.valueOf(args[1]));
////        boolean c = Source.getOption////        boolean c(Boolean.valueOf(args[2]));
        A = Source.getOptionA(Boolean.valueOf(args[0]));
        B = Source.getOptionB(Boolean.valueOf(args[1]));
        C = Source.getOptionC(Boolean.valueOf(args[2]));
//
//        boolean a;
//        boolean b;
//        boolean c;
//
//        if (A) {
//            a = true;
//        } else {
//            a = false;
//        }
//
//        if (B) {
//            b = true;
//        } else {
//            b = false;
//        }
//
//        if (C) {
//            c = true;
//        } else {
//            c = false;
//        }

        int i = 0;

        if(A) {
            Thread.sleep(100);
            i = 1;
        }

        if(B && i > 0) {
            Thread.sleep(200);
            integer(i);
        }

        if(C) {
            Thread.sleep(300);
            doubleNumber(3);
        }
    }

    private static void doubleNumber(double d) throws InterruptedException {
        if(d > 0) {
            Thread.sleep(400);
        }

    }

    private static void integer(int i) throws InterruptedException {
        if(i > 0) {
            Thread.sleep(500);
        }
    }
}

