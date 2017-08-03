package soot.jimple.infoflow.test.options.code.todo;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Sleep17 {

    public static final String FILENAME = Sleep17.class.getCanonicalName();
    public static final String PACKAGE = Sleep17.class.getPackage().getName();
    public static final String CLASS = Sleep17.class.getSimpleName();
    public static final String MAIN_METHOD = "main";
    public static final String METHOD_1 = "method1";
    public static final String METHOD_2 = "method2";
    public static boolean A = false;
    public static boolean B = false;
    public static boolean C = false;

    public static void main(String[] args) throws InterruptedException {
        // Region program start
        System.out.println("main");

//        boolean a = Boolean.valueOf(args[0]);
//        boolean b = Boolean.valueOf(args[1]);
//        boolean c = Boolean.valueOf(args[2]);
        A = Boolean.valueOf(args[0]);
        B = Boolean.valueOf(args[1]);
        C = Boolean.valueOf(args[2]);

        boolean a;

        if(A) {
            a = true;
        }
        else {
            a = false;
        }

        boolean b;

        if(B) {
            b = true;
        }
        else {
            b = false;
        }

        boolean c;

        if(C) {
            c = true;
        }
        else {
            c = false;
        }

        int i = 0;

        Thread.sleep(100);
        if(a) { // 28
            // Region A start
            Thread.sleep(200);
            Sleep17.method1(a);
            i += 2;
            // Region A end
        }
        else if(b) { // 42
            // Region B start
            Thread.sleep(300);
            Sleep17.method2(b);
            i += 3;
            // Region B end
        }

        Thread.sleep(400);

        if(c) {
            Thread.sleep(i * 100);
        }

        method1(false, "false");
    }

    public static void method1(boolean A) throws InterruptedException {
        System.out.println("method1");
        boolean a = A;
        Thread.sleep(500);

        // Region A start
        if(a) { // 16
            Thread.sleep(600);
        }
        // Region A end 20

        Thread.sleep(700);
    }

    public static void method1(boolean A, String check) throws InterruptedException {
        Thread.sleep(500);
    }

    public static void method2(boolean B) throws InterruptedException {
        System.out.println("method2");
        boolean b = B;
        Thread.sleep(800);

        // Region B start
        if(b) { // 16
            Thread.sleep(900);
        }
        // Region B end

        Thread.sleep(1000);
    }

}
