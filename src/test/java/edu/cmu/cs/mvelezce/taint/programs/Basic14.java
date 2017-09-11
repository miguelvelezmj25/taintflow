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
        // Object w is tainted by all 3 sources
        W w = new W();

        w.setA(a);
        w.setB(b);
        w.setC(c);

        if(w == null) {
            System.out.println(";");
        }

        if(w.isA()) {
            System.out.println(";");
        }

        if(w.isB()) {
            System.out.println(";");
        }

        if(w.isC()) {
            System.out.println(";");
        }

        w.analyze();
    }

    public class W {
        private boolean a = false;
        private boolean b = false;
        private boolean c = false;

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
            if(this.a) {
                System.out.println(":");
            }

            if(this.b) {
                System.out.println(":");
            }

            if(this.c) {
                System.out.println(":");
            }
        }
    }

}
