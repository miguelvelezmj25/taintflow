package soot.jimple.infoflow.test.options.code;

import soot.jimple.infoflow.test.options.Options;

public class Sleep2 {

    public static void main(String[] args) throws InterruptedException {
        Options options = new Options();
        boolean A = options.getOption();
        boolean B = false;
        int x = 0;

        if(options.getDecision(A)) {
            x++;
        }

        if(options.getDecision(B)) {
            x++;
        }

        if(options.getDecision(x > 0)) {
            x = -1;
        }
    }

}
