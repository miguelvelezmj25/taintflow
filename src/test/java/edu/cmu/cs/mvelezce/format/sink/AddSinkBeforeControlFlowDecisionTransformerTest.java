package edu.cmu.cs.mvelezce.format.sink;

import edu.cmu.cs.mvelezce.format.instrument.methodnode.MethodTransformer;
import edu.cmu.cs.mvelezce.format.instrument.methodnode.MethodTransformerBase;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class AddSinkBeforeControlFlowDecisionTransformerTest {

    @Test
    public void transform() throws InvocationTargetException, NoSuchMethodException, IOException, IllegalAccessException {
        String directory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/running-example/target/classes";

        MethodTransformer transformer = new AddSinkBeforeControlFlowDecisionTransformer(directory);
        transformer.transformMethods();
    }

}