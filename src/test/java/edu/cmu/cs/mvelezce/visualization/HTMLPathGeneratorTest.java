package edu.cmu.cs.mvelezce.visualization;

import org.junit.Test;

import java.io.IOException;

public class HTMLPathGeneratorTest {

    @Test
    public void generateHTMLRunningExample() throws IOException {
        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/running-example/src/main/java";
        String systemName = "running-example";

        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }

    @Test
    public void generateHTMLColorCounter() throws IOException {
        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/pngtastic-counter/src";
        String systemName = "pngtasticColorCounter";

        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }

    @Test
    public void generateHTMLOptimizer() throws IOException {
        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/pngtastic-optimizer/src";
        String systemName = "pngtasticOptimizer";

        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }

    @Test
    public void generateHTMLCommonsCompress() throws IOException {
        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/commons-compress/src/main/java";
        String systemName = "commonsCompress";

        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }

    @Test
    public void generateHTMLKanzi() throws IOException {
        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/kanzi/java/src";
        String systemName = "kanzi";

        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }

    @Test
    public void generateHTMLInteraction0() throws IOException {
        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/src/test/java/edu/cmu/cs/mvelezce/taint/programs";
        String systemName = "interaction0";

        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }

    @Test
    public void generateHTMLInteraction1() throws IOException {
        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/src/test/java/edu/cmu/cs/mvelezce/taint/programs";
        String systemName = "interaction1";

        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }

    @Test
    public void generateHTMLInteraction2() throws IOException {
        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/src/test/java/edu/cmu/cs/mvelezce/taint/programs";
        String systemName = "interaction2";

        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }

    @Test
    public void generateHTMLInteraction3() throws IOException {
        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/src/test/java/edu/cmu/cs/mvelezce/taint/programs";
        String systemName = "interaction3";

        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }

    @Test
    public void generateHTMLInteraction4() throws IOException {
        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/src/test/java/edu/cmu/cs/mvelezce/taint/programs";
        String systemName = "interaction4";

        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }

    @Test
    public void generateHTMLInteraction5() throws IOException {
        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/src/test/java/edu/cmu/cs/mvelezce/taint/programs";
        String systemName = "interaction5";

        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }

    @Test
    public void generateHTMLInteraction6() throws IOException {
        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/src/test/java/edu/cmu/cs/mvelezce/taint/programs";
        String systemName = "interaction6";

        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }
}