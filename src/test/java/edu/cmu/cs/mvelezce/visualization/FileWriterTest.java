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

}