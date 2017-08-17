package edu.cmu.cs.mvelezce.taint.programs;

import edu.cmu.cs.mvelezce.analysis.option.Source;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Basic20 {

    public static boolean A;

    public static void main(String[] args) throws InterruptedException {
        A = Source.getOptionA(true);

        boolean a = A;
        int i = 0;

        if(a) {
            i = 5;
        }
        else {
            i = 10;
        }

        while (i > 0) {
            i = i - 1;
        }

        for(i = 0; i < 10; i++) {
            i = i;
        }

        switch (i) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            default:
        }

        switch (i) {
            case 1:
                break;
            case 10:
                break;
            case 100:
                break;
            case 1000:
                break;
            default:
        }

        try {
            i = i / 0;
        }
        catch(Exception e) {
            throw new RuntimeException("Dfs");
        }

        if(i == 0) {
            i = 1;
        }
    }

}

