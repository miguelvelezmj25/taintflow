package soot.jimple.infoflow.test.options.code;

import soot.jimple.infoflow.test.options.Options;

public class Basic4 {

    private static Options options = new Options();

    public static void main(String[] args) throws InterruptedException {
        boolean A = Basic4.options.getOption();
        Basic4.foo(A);
    }

    public static void foo(boolean x) {
        int i = 0;

        if(Basic4.options.getDecision(x)) {
            i++;
        }
    }

}
