package soot.jimple.infoflow.test.options.code.todo;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Sleep19 {

    public static boolean A = false;

    public static void main(String[] args) throws InterruptedException {
        A = Boolean.valueOf(args[0]);

        boolean a;

        if(A) {
            a = true;
        }
        else {
            a = false;
        }

        boolean x = a;

        if(x) {
            x = !x;
        }
    }
}
