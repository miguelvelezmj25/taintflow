package edu.cmu.cs.mvelezce.visualization;

import org.junit.Test;

import java.io.IOException;

public class HTMLPathGeneratorTest {

    @Test
    public void generateHTMLRunningExample() throws IOException {
        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/running-example/src/main/java";
        String systemName = "running-example";

        HTMLPathGenerator.something(root, systemName);

//        HTMLBaseGenerator generator = new HTMLPathGenerator(root, systemName);
//        generator.generateHTMLPage();
    }

}