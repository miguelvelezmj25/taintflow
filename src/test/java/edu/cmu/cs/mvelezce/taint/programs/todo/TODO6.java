package edu.cmu.cs.mvelezce.taint.programs.todo;

/**
 * Created by mvelezce on 6/16/17.
 */
public class TODO6 {

    public static boolean ON = false;
    public static int integerFeature = 5;
    public static double doubleFeature = 5;

    public static void main(String[] args) throws InterruptedException {
        integer(2);
        Thread.sleep(100);
        double d = 0;

        if(ON) {
            Thread.sleep(200);
            d = 1;
        }
        else if(integerFeature > 4 && doubleFeature > 2) {
            integer(1);
            Thread.sleep(500);
        }

        doubleNumber(d);
    }

    private static void doubleNumber(double d) throws InterruptedException {
        if(d > 0) {
            Thread.sleep(600);
        }

    }

    private static void integer(int i) throws InterruptedException {
        if(i > 0) {
            Thread.sleep(700);
        }
    }
}
