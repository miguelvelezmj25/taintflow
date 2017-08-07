package edu.cmu.cs.mvelezce.format;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class SourceTest {

    @Test
    public void format1() throws IOException {
        String fileName = "/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/src/test/java/edu/cmu/cs/mvelezce/taint/programs/todo/Union1.java";
        Source.format(fileName);
    }

    @Test
    public void format2() throws IOException {
        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/src/test/java/edu/cmu/cs/mvelezce/taint/programs/todo/";
        Collection<File> files = FileUtils.listFiles(new File(root), new String[] {"java"}, true);

        for(File file : files) {
            Source.format(file.getAbsolutePath());
        }
    }

}