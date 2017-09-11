package edu.cmu.cs.mvelezce.format.sink;

import edu.cmu.cs.mvelezce.format.instrument.methodnode.MethodTransformer;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class AddSinkBeforeControlFlowDecisionTransformerTest {

    @Test
    public void transformTest1() throws InvocationTargetException, NoSuchMethodException, IOException, IllegalAccessException {
        String directory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/running-example/target/classes";

        MethodTransformer transformer = new AddSinkBeforeControlFlowDecisionTransformerMethodTransformer(directory);
        transformer.transformMethods();
    }

    @Test
    public void transformTest2() throws InvocationTargetException, NoSuchMethodException, IOException, IllegalAccessException {
        String directory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/pngtastic-counter/out/production/pngtastic-counter";

        MethodTransformer transformer = new AddSinkBeforeControlFlowDecisionTransformerMethodTransformer(directory);
        transformer.transformMethods();
    }

}