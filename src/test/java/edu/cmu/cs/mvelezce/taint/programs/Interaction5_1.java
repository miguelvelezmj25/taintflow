package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Sink;
import edu.cmu.cs.mvelezce.analysis.option.Source;

import javax.management.RuntimeErrorException;
import java.io.FileNotFoundException;
import java.io.PrintStream;

/*
    No edge is create from the throw to the catch since the tyep of exception that is thrown is not caught.

    There are 2 correct results
 */
public class Interaction5_1 {

    public static void main(String[] args) {
        Sink.init();

        boolean A = Source.getOptionA(true);

        boolean a = false;
        boolean x = false;

        if(A) {
            a = true;
        }

        try {
            int i = 0;

            if(a) {
                i = i % 2;
            }

            throw new RuntimeException();
        } catch (ArithmeticException e) {
            Sink.sink(a);
            Sink.sink(x);
            Sink.sink(e);
            Math.random();
        }

        if(x) {
            Math.random();
        }
    }
}
