package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

/*
    Exceptions
 */
public class Interaction4 {

    public static void main(String[] args) {
        Sink.init();

        boolean A = Source.getOptionA(true);
        boolean B = Source.getOptionB(true);

        boolean a = false;
        boolean b = false;

        if(A) {
            a = true;
        }

        if(B) {
            b = true;
        }

        try {
            int i = 0;
            if(a) {
                System.out.println("");
            }

            throw new RuntimeException();
        } catch (Exception e) {
            System.out.println();
        }

        if(b) {
            System.out.println("");
        }

        FOO f = new FOO(A,B);
        if(f == null) {
            System.out.println("");
        }
        f.count(100);

    }

    private static class FOO {
        private final boolean x;
        private final boolean y;
        private Log log = new Log();

        FOO(boolean x, boolean y) {
            this.x
                    =x;
            this.y=y;
        }

        void count(int y) {
            log.log(y);
        }
    }
    private static class Log{
        public void log(int y) {
            if (y>0) {
                System.out.println("...");
            }
        }
    }

}
