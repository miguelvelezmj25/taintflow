package soot.jimple.infoflow.test.options.code;

import soot.jimple.infoflow.test.options.Options;

public class Basic0 {

    public static void main(String[] args) throws InterruptedException {
        Options options = new Options();
        boolean A = options.getOption();
        int x;

        if(options.getDecision(A)) {
            x = 0;
        }
        else {
            x = 1;
        }
    }

}
