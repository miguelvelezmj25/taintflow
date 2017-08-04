package edu.cmu.cs.mvelezce.taint.programs.todo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mvelezce on 3/31/17.
 */
public class TODO4 {

    private static boolean FEATURE_A;

    public static void main(String[] args) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//
//        switch (Integer.valueOf(args[0])) {
//            case 1:
//                TODO4.inc1(1);
//                break;
//            case 2:
//                TODO4.inc2(2);
//                break;
//        }
//    }
//
//    public static void inc1(int a) {
//        int result = a + 1;
//        System.out.println(result);
//    }
//
//    public static void inc2(int a) {
//        int result = a + 1;
//        System.out.println(result);
    }


    public static List<String> test() throws InterruptedException {
        return sleep(true);
    }

    public static List<String> test1() throws InterruptedException {
        List<String> r = sleep(false);
        return r;
    }

    public static StringBuilder test2() throws InterruptedException {
        int i = 0;
        StringBuilder s = new StringBuilder();

        if(i > 0) {
            return s.append("SDF");
        }
        return s.append("DSFSF'");
    }

    public static List<String> sleep(boolean x) throws InterruptedException {
        Thread.sleep(5);
        return new ArrayList<>();
    }

    public static int integer() {
        return 2123;
    }

    public static void m() {
        System.out.println("ASFdsf");
    }
}
