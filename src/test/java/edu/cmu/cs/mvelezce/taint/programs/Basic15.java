package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Basic15 {

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

        if(A) {
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

        new Basic15(a, b, c);
    }


    public Basic15(boolean a, boolean b, boolean c) {
        W w = new W();

        w.addOption(a);
        w.addOption(b);
        w.addOption(c);

        if(Sink.getDecision(w == null)) {
            System.out.println(";");
        }


        if(Sink.getDecision(w.getOptions().isEmpty())) {
            System.out.println(";");
        }


        if(Sink.getDecision(w.getOptions().iterator().next())) {
            System.out.println(";");
        }


        w.analyze();
    }

    public class W {
        private Set<Boolean> options = new HashSet<>();

        public W() {
            ;
        }

        public Set<Boolean> getOptions() {
            return options;
        }

        public void setOptions(Set<Boolean> options) {
            this.options = options;
        }

        public void addOption(boolean option) {
            if(Sink.getDecision(option)) {
                System.out.println(":");
            }

            this.options.add(option);

            if(Sink.getDecision(this.options.isEmpty())) {
                System.out.println(":");
            }
        }

        public void analyze() {
            if(Sink.getDecision(this.options.isEmpty())) {
                System.out.println(":");
            }


            for(Boolean b : this.options) {
                if(Sink.getDecision(b)) {
                    System.out.println(":");
                }
            }
        }
    }

}
