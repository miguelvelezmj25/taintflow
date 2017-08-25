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

    @Test
    public void generateHTMLOptimizer() throws IOException {
        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/pngtastic-optimizer/src";
        String systemName = "pngtasticOptimizer";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
    }

    @Test
    public void generateHTMLInteraction0() throws IOException {
        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/src/test/java/edu/cmu/cs/mvelezce/taint/programs";
        String systemName = "interaction0";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
    }

    @Test
    public void generateHTMLInteraction1() throws IOException {
        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/src/test/java/edu/cmu/cs/mvelezce/taint/programs";
        String systemName = "interaction1";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
    }

    @Test
    public void generateHTMLInteraction2() throws IOException {
        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/src/test/java/edu/cmu/cs/mvelezce/taint/programs";
        String systemName = "interaction2";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
    }

    @Test
    public void generateHTMLInteraction3() throws IOException {
        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/src/test/java/edu/cmu/cs/mvelezce/taint/programs";
        String systemName = "interaction3";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
    }

    @Test
    public void generateHTMLInteraction4() throws IOException {
        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/src/test/java/edu/cmu/cs/mvelezce/taint/programs";
        String systemName = "interaction4";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
    }

    @Test
    public void generateHTMLInteraction5() throws IOException {
        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/src/test/java/edu/cmu/cs/mvelezce/taint/programs";
        String systemName = "interaction5";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
    }

    @Test
    public void generateHTMLInteraction6() throws IOException {
        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/src/test/java/edu/cmu/cs/mvelezce/taint/programs";
        String systemName = "interaction6";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
    }

    @Test
    public void generateHTMLInteraction7() throws IOException {
        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/src/test/java/edu/cmu/cs/mvelezce/taint/programs";
        String systemName = "interaction7";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
    }

    @Test
    public void generateHTMLSleep0() throws IOException {
        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/src/test/java/edu/cmu/cs/mvelezce/taint/programs";
        String systemName = "sleep0";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
    }

    @Test
    public void generateHTMLProblem2() throws IOException {
        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/src/test/java/edu/cmu/cs/mvelezce/taint/programs";
        String systemName = "problem2";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
    }

}