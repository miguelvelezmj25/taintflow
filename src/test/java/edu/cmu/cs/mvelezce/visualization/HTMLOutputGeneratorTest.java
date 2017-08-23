package edu.cmu.cs.mvelezce.visualization;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class HTMLOutputGeneratorTest {
    @Test
    public void generateHTMLRunningExample() throws IOException {
        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/running-example/src/main/java";
        String systemName = "running-example";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
    }

    @Test
    public void generateHTMLColorCounter() throws IOException {
        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/pngtastic-counter/src";
        String systemName = "pngtasticColorCounter";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
    }

}