package soot.jimple.infoflow.test.options.code.todo;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Sleep20 {

    public static boolean A = false;

    public static void main(String[] args) throws InterruptedException {
        int i = 2;

        switch (i) {
            case 0:
                i = 0;
                break;
            case 1:
                i = 1;
                break;
            default:
                i = -1;
        }

        System.out.println(i);

        method(false);
        method(false, false);
        method("false", false);
    }

    public static void method(boolean fail) {
        if(fail) {
            throw new IllegalArgumentException("You failed in life");
        }

        System.out.println("You can still fail later");
    }

    public static void method(boolean[] fail, boolean succeed) {
        System.out.println("You can still fail later");
    }

    public static void method(boolean fail, boolean succeed) {
        System.out.println("You can still fail later");
    }

    public static void method(String fail, boolean succeed) {
        System.out.println("You can still fail later");
    }

    public void method(String fail, String succeed) {
        System.out.println("You can still fail later");
    }
}
