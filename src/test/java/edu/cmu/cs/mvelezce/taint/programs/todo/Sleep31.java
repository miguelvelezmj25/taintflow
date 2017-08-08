package edu.cmu.cs.mvelezce.taint.programs.todo;

import edu.cmu.cs.mvelezce.analysis.option.Source;

public class Sleep31 {

    public static boolean A = false;
    public static boolean B = false;
    public static boolean Z = false;

    public static void main(String[] args) throws InterruptedException {
//        if(Z) {
//            return;
//        }
//        else {
        start(args);
//        }
    }

    private static void start(String[] args) throws InterruptedException {
        A = Source.getOptionA(Boolean.valueOf(args[0]));
        B = Source.getOptionB(Boolean.valueOf(args[1]));

        boolean a;
        boolean b;

        if(A) {
            a = true;
        }
        else {
            a = false;
        }

        if(B) {
            b = true;
        }
        else {
            b = false;
        }

        Thread.sleep(200);

        if(a) {
            Thread.sleep(100);
            m1();
        }

//        if(false) {
        m1();
//            m1(false);
//        }
        if(b) {
            Thread.sleep(300);
            m1();
        }


//        m1();
//        m1(false);
    }

    public static void m1(/*boolean x*/) throws InterruptedException {
        Thread.sleep(500);
        Thread.sleep(500);
        Thread.sleep(500);
        Thread.sleep(500);

//        if(x) {
//            Thread.sleep(200);
//        }
    }

}



/*
public class Sleep31 {

    public static boolean A = false;
    public static boolean B = false;
    public static boolean Z = false;

    public static void main(String[] args) throws InterruptedException {
//        if(Z) {
//            return;
//        }
//        else {
        start(args);
//        }
    }

    private static void start(String[] args) throws InterruptedException {
A = SourceFormatter.getOptionA(Boolean.valueOf(args[0]));
B = SourceFormatter.getOptionB(Boolean.valueOf(args[1]));

        boolean a;
//        boolean b;
        int b;

        if(A) {
            a = true;
        }
        else {
            a = false;
        }

        if(B) {
            b = 10;
        }
        else {
            b = -10;
        }

        Thread.sleep(200);

        if(a) {
            Thread.sleep(100);
            m1(a);
        }

//        if(false) {
//            m1(true);
//            m1(false);
//        }
        for(int i = 0; i < b; i++) {
            Thread.sleep(300);
//            m1(b);
        }


//        m1();
//        m1(false);
    }

    public static void m1(boolean x) throws InterruptedException {
        Thread.sleep(500);
        Thread.sleep(500);
        Thread.sleep(500);
        Thread.sleep(500);

//        if(x) {
//            Thread.sleep(200);
//        }
    }

}

 */
