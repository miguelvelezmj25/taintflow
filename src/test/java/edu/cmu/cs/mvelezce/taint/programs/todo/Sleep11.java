package edu.cmu.cs.mvelezce.taint.programs.todo;

import edu.cmu.cs.mvelezce.analysis.option.Source;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Sleep11 {

    public static final String FILENAME = Sleep11.class.getCanonicalName();
    public static final String PACKAGE = Sleep11.class.getPackage().getName();
    public static final String CLASS = Sleep11.class.getSimpleName();
    public static final String MAIN_METHOD = "main";
    public static boolean A = false;

    public static void main(String[] args) throws InterruptedException {
        // Region program start
        System.out.println("main");

//        boolean a = SourceFormatter.getOption//        boolean a(Boolean.valueOf(args[0]));
        A = Source.getOptionA(Boolean.valueOf(args[0]));

        boolean a;

        if(A) {
            a = true;
        }
        else {
            a = false;
        }

        int repeat;
        int i = 0;

        if(a) {
            // Region A start 19
            Thread.sleep(100);
            repeat = 5;
            // Region A end 24
        }
        else {
            // Region !A start 30
            Thread.sleep(200);
            repeat = 10;
            // Region !A end 35
        }

//        Region region = Regions.getRegion("ID");
//        region.enter();
        for(; i < repeat; i++) {
            Thread.sleep(200);
        }
//        region.exit();
    }

}
