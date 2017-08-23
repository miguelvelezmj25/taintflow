package edu.cmu.cs.mvelezce.visualization;

import edu.cmu.cs.mvelezce.analysis.option.json.ControlFlowResult;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class FileWriterTest {

    @Test
    public void deleteAnnotatedFiles() throws IOException {
        String directory = "/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/src/test/java/edu/cmu/cs/mvelezce/taint/programs";
        FileWriter.deleteAnnotatedFiles(directory);
    }

    @Test
    public void readResultsTest() throws IOException {
        FileWriter fw = new FileWriter();
        List<ControlFlowResult> result = fw.readResults("kanzi");
        Assert.assertFalse(result.isEmpty());
    }

    @Test
    public void writeFileWithResultsTest() throws IOException {
        String directory = "/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/src/test/java/edu/cmu/cs/mvelezce/taint/programs";
        FileWriter fw = new FileWriter();
        List<ControlFlowResult> results = fw.readResults("basic1");

        fw.writeAnnotatedFile(directory, results);
    }

    @Test
    public void annotateRunningExample() throws IOException {
        String system = "running-example";
        String directory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/running-example/src/main/java";
        FileWriter fw = new FileWriter();

        fw.writeAnnotatedFile(system, directory);
    }

    @Test
    public void annotateColorCounter() throws IOException {
        String system = "pngtasticColorCounter";
        String directory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/pngtastic-counter/src";
        FileWriter fw = new FileWriter();

        fw.writeAnnotatedFile(system, directory);
    }

    @Test
    public void deleteAnnotatedRunningExample() throws IOException {
        String directory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/pngtastic-counter/src";
        FileWriter.deleteAnnotatedFiles(directory);
    }

}