package edu.cmu.cs.mvelezce.analysis;

import soot.Body;
import soot.BodyTransformer;

import java.util.Map;

public class IfSink extends BodyTransformer {

    private static IfSink instance = new IfSink();

    private IfSink() { ; }

    public static IfSink v() {
        return IfSink.instance;
    }

    @Override
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        System.out.println(b.getMethod().getSignature());
    }
}
