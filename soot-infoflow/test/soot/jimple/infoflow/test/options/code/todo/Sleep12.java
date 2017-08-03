package soot.jimple.infoflow.test.options.code.todo;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Sleep12 {

    public static final String FILENAME = Sleep12.class.getCanonicalName();
    public static final String PACKAGE = Sleep12.class.getPackage().getName();
    public static final String CLASS = Sleep12.class.getSimpleName();
    public static final String MAIN_METHOD = "main";
    public static boolean A = false;

    public static void main(String[] args) throws InterruptedException {
        // Region program start
        System.out.println("main");

//        boolean a = Boolean.valueOf(args[0]);
        A = Boolean.valueOf(args[0]);

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

        // TODO lotrack's output is wrong
//        Region region = Regions.getRegion("ID");
//        region.enter();
        for(; i < repeat; i++) {
            // Region A start
            Thread.sleep(200);
            // Region A end
        }
//        region.exit();
    }

}
