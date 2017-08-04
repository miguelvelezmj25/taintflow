package edu.cmu.cs.mvelezce.taint.programs.todo;

/**
 * Created by miguelvelez on 6/14/17.
 */
public class Feature {

    public static boolean ONE = false;

    public static void main(String[] args) {
        ONE = true;
        int i = 0;

        if(ONE) {
            i = 1;
        }
        else {
            i = 2;
        }

        i += 5;
    }
}
