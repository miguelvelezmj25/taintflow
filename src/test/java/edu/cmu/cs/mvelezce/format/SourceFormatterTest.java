package edu.cmu.cs.mvelezce.format;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class SourceFormatterTest {
    @Test
    public void testCompile1() throws IOException, InterruptedException {
        String fileName = "/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/src/test/java/edu/cmu/cs/mvelezce/taint/programs/todo/Sleep4.java";
        SourceFormatter.compile(fileName, "/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/target/test-classes");
    }

    @Test
    public void testFormatSources1() throws IOException {
        String fileName = "/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/src/test/java/edu/cmu/cs/mvelezce/taint/programs/todo/Union1.java";
        SourceFormatter.formatSources(fileName);
    }

    @Test
    public void testFormatSources2() throws IOException {
        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/src/test/java/edu/cmu/cs/mvelezce/taint/programs/todo/";
        Collection<File> files = FileUtils.listFiles(new File(root), new String[]{"java"}, true);

        for(File file : files) {
            SourceFormatter.formatSources(file.getAbsolutePath());
        }
    }

    @Test
    public void format() throws IOException, InterruptedException {
        String fileName = "/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/src/test/java/edu/cmu/cs/mvelezce/taint/programs/todo/Sleep4.java";
        String srcDir = "/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/target/test-classes";
        SourceFormatter.format(fileName, srcDir);
    }

}