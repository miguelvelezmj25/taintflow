package soot.jimple.infoflow.test.options.code.todo;


public class Union01 {

    public static boolean A = false;
    public static boolean B = false;

    public static void main(String[] args) throws InterruptedException {
        A = Boolean.valueOf(args[0]);
        B = Boolean.valueOf(args[1]);

        boolean a;
        boolean b;

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

        Thread.sleep(1);

        if(a) {
            Thread.sleep(2);
            m1();
        }

        if(b) {
            Thread.sleep(3);
            m1();
        }

        if(a) {
            m1();
        }

        Thread.sleep(4);
    }

    public static void m1() throws InterruptedException {
        Thread.sleep(5);
        Thread.sleep(6);
        Thread.sleep(7);
        Thread.sleep(8);
    }

}