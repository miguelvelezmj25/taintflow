package soot.jimple.infoflow.test.options;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import soot.jimple.infoflow.Infoflow;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.results.InfoflowResults;
import soot.jimple.infoflow.taintWrappers.EasyTaintWrapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class OptionsTest {

    protected static String appPath;
    protected static String libPath;
    protected static List<String> sources;
    protected static List<String> sinks;
    protected static InfoflowConfiguration infoflowConfiguration;

    @BeforeClass
    public static void setUp() throws IOException {
        File f = new File("./soot-infoflow/");
        File testSrc1 = new File(f, "bin");

        if(!testSrc1.exists()) {
            fail("Test aborted - none of the test OptionsTest.sources are available");
        }

        OptionsTest.appPath = testSrc1.getCanonicalPath();
        OptionsTest.libPath = System.getProperty("java.home") + File.separator + "lib" + File.separator + "rt.jar";

        // Config information flow
        OptionsTest.infoflowConfiguration = new InfoflowConfiguration();
        OptionsTest.infoflowConfiguration.setCallgraphAlgorithm(InfoflowConfiguration.CallgraphAlgorithm.CHA);
        OptionsTest.infoflowConfiguration.setEnableImplicitFlows(true);
        OptionsTest.infoflowConfiguration.setCodeEliminationMode(InfoflowConfiguration.CodeEliminationMode.NoCodeElimination);
        OptionsTest.infoflowConfiguration.setInspectSinks(true);
    }

    @Before
    public void beforeTest() {
        OptionsTest.sources = new ArrayList<>();
        OptionsTest.sources.add("<soot.jimple.infoflow.test.options.Options: boolean getOption()>");

        OptionsTest.sinks = new ArrayList<>();
        OptionsTest.sinks.add("<soot.jimple.infoflow.test.options.Options: boolean getDecision(boolean)>");
    }

    protected void checkInfoflow(Infoflow infoflow, int resultCount) {
        if(infoflow.isResultAvailable()) {
            InfoflowResults map = infoflow.getResults();
            boolean containsSink = false;
            List<String> actualSinkStrings = new LinkedList<String>();
            assertEquals(resultCount, map.size());
            for(String sink : OptionsTest.sinks) {
                if(map.containsSinkMethod(sink)) {
                    containsSink = true;
                    actualSinkStrings.add(sink);
                }
            }

            assertTrue(containsSink);
            boolean onePathFound = false;
            for(String sink : actualSinkStrings) {
                boolean hasPath = false;
                for(String source : OptionsTest.sources) {
                    if(map.isPathBetweenMethods(sink, source)) {
                        hasPath = true;
                        break;
                    }
                }
                if(hasPath) {
                    onePathFound = true;
                }
            }
            assertTrue(onePathFound);

        }
        else {
            fail("result is not available");
        }
    }

    protected void negativeCheckInfoflow(Infoflow infoflow) {
        if(infoflow.isResultAvailable()) {
            InfoflowResults map = infoflow.getResults();
            for(String sink : OptionsTest.sinks) {
                if(map.containsSinkMethod(sink)) {
                    fail("sink is reached: " + sink);
                }
            }
            assertEquals(0, map.size());
        }
        else {
            fail("result is not available");
        }
    }

    @Test
    public void sleep0Test() throws IOException {
        Infoflow infoflow = new Infoflow();
        infoflow.setConfig(OptionsTest.infoflowConfiguration);

        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
        infoflow.setTaintWrapper(easyWrapper);

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add("<soot.jimple.infoflow.test.options.code.Basic0: void main(java.lang.String[])>");

        infoflow.computeInfoflow(OptionsTest.appPath, OptionsTest.libPath, entryPoints, OptionsTest.sources, OptionsTest.sinks);
        checkInfoflow(infoflow, 1);
    }

    @Test
    public void sleep1Test() throws IOException {
        Infoflow infoflow = new Infoflow();
        infoflow.setConfig(OptionsTest.infoflowConfiguration);

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add("<soot.jimple.infoflow.test.options.code.Basic1: void main(java.lang.String[])>");

        infoflow.computeInfoflow(OptionsTest.appPath, OptionsTest.libPath, entryPoints, OptionsTest.sources, OptionsTest.sinks);
        checkInfoflow(infoflow, 2);
    }

    @Test
    public void sleep2Test() throws IOException {
        Infoflow infoflow = new Infoflow();
        infoflow.setConfig(OptionsTest.infoflowConfiguration);

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add("<soot.jimple.infoflow.test.options.code.Basic2: void main(java.lang.String[])>");

        infoflow.computeInfoflow(OptionsTest.appPath, OptionsTest.libPath, entryPoints, OptionsTest.sources, OptionsTest.sinks);
        checkInfoflow(infoflow, 2);
    }

}
