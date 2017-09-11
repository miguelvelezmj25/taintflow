package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Basic19 {

    public static boolean A;
    public static boolean B;

    public static void main(String[] args) throws InterruptedException {
        A = Source.getOptionA(true);
        B = Source.getOptionB(true);

        int a = 0;
        String log = "";

        if(A) {
            a = 1;
        }

        if(B) {
            log = "some";
        }

        X work = new X(a, log);

        Sink.sink(work == null);
        Sink.sink(work.a == 0);
        Sink.sink(work.b == null);

        work.work();

        Sink.sink(work.integer() == 0);
    }

    public static class X {
        private int a;
        private Y b;

        public X(int a, String b) {
            this.a = a;
            this.b = new Y(b);
        }

        public void work() {
            this.b.error();
        }

        public int integer() {
            return 5;
        }
    }

    public static class Y {
        private String l;

        public Y(String logger) {
            this.l = logger;
        }

        public void error() {
            Sink.sink(this.l.isEmpty());
        }

    }

}

