package soot.jimple.infoflow.test.options.code.todo;

/**
 * Created by mvelezce on 6/21/17.
 */
public class TODO8 {

    public int one(boolean a) {
        int i = 0;

        if(a) {
            return 1;
        }
        else {
            i = 0;
        }

        return i;
    }

    public int two(boolean a) {
        if(a) {
            return 1;
        }
        else {
            return 0;
        }
    }

    public int three(boolean a) {
        int i = 0;

        if(a) {
            i = 1;
        }
        else {
            i = 0;
        }

        return i;
    }

    public int four(boolean a, boolean b) {
        int i = 0;

        if(a) {
            i = 1;
        }
        else {
            if(b) {
                i = 3;
            }
            else {
                i = 0;
            }
        }

        return i;
    }
}
