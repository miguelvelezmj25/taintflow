package soot.jimple.infoflow.test.options.code.todo;

/**
 * Created by mvelezce on 4/21/17.
 */
public class TODO0 {

    public static boolean A = false;
    public static boolean B = false;
    public static boolean C = false;
    public static boolean D = false;
    public static boolean E = false;

    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(200);

        if(A) {
            Thread.sleep(100);
            m1();
        }

        if(B) {
            Thread.sleep(300);
            m1();
        }

        m1();
    }

    public static void m1() throws InterruptedException {
        Thread.sleep(500);

        if(C) {
            Thread.sleep(400);
            int a = 0;
            a = a + 5;
            a = a - 2;
//            m2();
        }

//        if(D) {
//            m2();
//        }
    }

    public static void m2() {
        if(E) {
            int a = 0;
            a = a + 5;
            a = a - 2;
        }
    }

//    public static void main(String[] args)  {
//        if(A) {
//            String s = "";
//        }
//    }

}
