package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Basic14 {

    public static boolean A;
    public static boolean B;
    public static boolean C;

    public static void main(String[] args) throws InterruptedException {
        A = Source.getOptionA(Boolean.valueOf(args[0]));
        B = Source.getOptionB(Boolean.valueOf(args[1]));
        C = Source.getOptionC(Boolean.valueOf(args[2]));

        boolean a;
        boolean b;
        boolean c;

        if(A)  {
            a = true;
        }
        else {
            a = false;
        }

        if(B) {
            b = true;
        }
        else {
            b = false;
        }

        if(C) {
            c = true;
        }
        else {
            c = false;
        }

        new Basic14(a, b, c);
    }


    public Basic14(boolean a, boolean b, boolean c) {
        W w = new W();

        w.setA(a);
        w.setB(b);
        w.setC(c);

        if(Sink.getDecision(w == null)) {
            System.out.println(";");
        }

        if(Sink.getDecision(w.isA())) {
            System.out.println(";");
        }

        if(Sink.getDecision(w.isB())) {
            System.out.println(";");
        }

        if(Sink.getDecision(w.isC())) {
            System.out.println(";");
        }

        w.analyze();
    }

    public class W {
        private boolean a = false;
        private boolean b = false;
        private boolean c = false;

        public W(boolean a, boolean b, boolean c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        public W() {
            ;
        }

        public void setA(boolean a) {
            this.a = a;
        }

        public void setB(boolean b) {
            this.b = b;
        }

        public void setC(boolean c) {
            this.c = c;
        }

        public boolean isA() {
            return a;
        }

        public boolean isB() {
            return b;
        }

        public boolean isC() {
            return c;
        }

        public void analyze() {
            if(Sink.getDecision(this.a)) {
                System.out.println(":");
            }

            if(Sink.getDecision(this.b)) {
                System.out.println(":");
            }

            if(Sink.getDecision(this.c)) {
                System.out.println(":");
            }
        }
    }

}
