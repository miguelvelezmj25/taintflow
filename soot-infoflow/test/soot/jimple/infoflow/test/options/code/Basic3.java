package soot.jimple.infoflow.test.options.code;

import soot.jimple.infoflow.test.options.Options;

public class Basic3 {

    private static Options options = new Options();

    public static void main(String[] args) throws InterruptedException {
        boolean A = Basic3.options.getOption();

        if(options.getDecision(A)) {
            Basic3.foo();
        }
    }

    public static void foo() {
        int x = 0;

        if(Basic3.options.getDecision(0 > 2)) {
            x++;
        }
    }

}
