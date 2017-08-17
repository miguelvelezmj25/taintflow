package edu.cmu.cs.mvelezce.analysis;

import org.junit.Test;

import static org.junit.Assert.*;

public class TaintInfoflowTest {

    @Test
    public void aggregateInfoflowResultsTest() throws Exception {
        TaintInfoflow infoflow = new TaintInfoflow("running-example");
        infoflow.aggregateInfoflowResults();
    }

}