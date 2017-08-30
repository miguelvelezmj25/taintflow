package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

/*
    Exceptions
 */
public class Interaction510 {

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
            if(a) {
                System.out.println("das");
            }

            throw new RuntimeException();
////            Thread.sleep(100);
//            System.out.println("");
        } catch (Exception e) {
            System.out.println();
        }

        if(a) {
            System.out.println("");
        }

        if(b) {
            System.out.println("");
        }

        FOO f = new FOO(A,B);
        if(f == null) {
            System.out.println("");
        }
        int i = 100;
        Sink.sink(i);
        f.count(i);

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
            Sink.sink(y);
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
