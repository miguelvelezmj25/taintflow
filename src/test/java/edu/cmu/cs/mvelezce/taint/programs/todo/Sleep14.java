package edu.cmu.cs.mvelezce.taint.programs.todo;

import edu.cmu.cs.mvelezce.analysis.option.Source;

/**
 * Created by mvelezce on 6/16/17.
 */
public class Sleep14 {

    public static final String FILENAME = Sleep14.class.getCanonicalName();
    public static final String PACKAGE = Sleep14.class.getPackage().getName();
    public static final String CLASS = Sleep14.class.getSimpleName();
    public static final String MAIN_METHOD = "main";
    public static boolean A = false;
    public static boolean B = false;
    public static boolean C = false;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("main");

//        boolean a = Source.getOption//        boolean a(Boolean.valueOf(args[0]));
//        int ia = Source.getOption//        int ia(Boolean.valueOf(args[4]));
//        double da = Source.getOption//        double da(Boolean.valueOf(args[5]));
        A = Source.getOptionA(Boolean.valueOf(args[0]));
        B = Source.getOptionB(Boolean.valueOf(args[4]));
        C = Source.getOptionC(Boolean.valueOf(args[5]));

        boolean a;
        int b;
        double c;

        if(A) {
            a = true;
        }
        else {
            a = false;
        }

        if(B) {
            b = 0;
        }
        else {
            b = 5;
        }

        if(C) {
            c = 0;
        }
        else {
            c = 5;
        }

        integer(2);
        Thread.sleep(100);
        double d = 0;

        if(a) {
            Thread.sleep(200);
            d = 1;
        }
        else if(b > 4 && c > 2) {
            integer(1);
            Thread.sleep(300);
        }

        doubleNumber(d);
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

