package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Source;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Basic21 {

    public static void main(String[] args)  {
        boolean a = Source.getOptionA(true);

        String s = "" + a;

        if(s == null) {
            System.out.println();
        }
    }

}

