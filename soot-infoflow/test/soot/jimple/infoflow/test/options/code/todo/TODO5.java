package soot.jimple.infoflow.test.options.code.todo;

/**
 * Created by mvelezce on 6/16/17.
 */
public class TODO5 {

    private static boolean A = true;

    public static void main(String[] args) {
        int a = A ? 1 : 0;

        if(a == 0) {
            System.out.println("foo");
        }


        new X(a);
        new Y().foo(a);
//        new W(a);
//        new Z().foo(a);
    }


}

//class W {
//
//    public W(int a) {
//
//        if (a == 0)
//            System.out.println("foo");
//    }
//}
//
//class Z {
//    public void foo(int a) {
//        if (a == 0)
//            System.out.println("foo");
//
//    }
//}
