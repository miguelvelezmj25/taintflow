package edu.cmu.cs.mvelezce.taint;

import edu.cmu.cs.mvelezce.analysis.TaintInfoflow;
import edu.cmu.cs.mvelezce.soot.SootConfig;
import org.junit.BeforeClass;
import org.junit.Test;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.config.IInfoflowConfig;
import soot.jimple.infoflow.results.InfoflowResults;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class AnalysisTest {

    protected static final String sep = System.getProperty("path.separator");
    protected static String appPath;
    protected static String libPath;
    //    protected static List<String> sources;
//    protected static List<String> sinks;
    protected static InfoflowConfiguration infoflowConfiguration;
    protected static IInfoflowConfig sootConfiguration;

    @BeforeClass
    public static void setUp() throws IOException {
        File f = new File(".");
        File testSrc1 = new File(f, "target" + File.separator + "classes");
        File testSrc2 = new File(f, "target" + File.separator + "test-classes");

        f = new File("./soot-infoflow/");
        File testSrc3 = new File(f, "bin");

        if(!(testSrc1.exists() || testSrc2.exists() || testSrc3.exists())) {
            fail("Test aborted - none of the test sources are available");
        }

        AnalysisTest.appPath = testSrc1.getCanonicalPath() + AnalysisTest.sep + testSrc2.getCanonicalPath() + AnalysisTest.sep + testSrc3.getCanonicalPath();
        AnalysisTest.libPath = System.getProperty("java.home") + File.separator + "lib" + File.separator + "rt.jar";

        // Config information flow
        AnalysisTest.infoflowConfiguration = new InfoflowConfiguration();
        AnalysisTest.infoflowConfiguration.setCallgraphAlgorithm(InfoflowConfiguration.CallgraphAlgorithm.CHA);
        AnalysisTest.infoflowConfiguration.setEnableImplicitFlows(true);
        AnalysisTest.infoflowConfiguration.setCodeEliminationMode(InfoflowConfiguration.CodeEliminationMode.NoCodeElimination);
        AnalysisTest.infoflowConfiguration.setInspectSinks(true);
        AnalysisTest.infoflowConfiguration.setAccessPathLength(1_000_000);

        // Config soot
        AnalysisTest.sootConfiguration = new SootConfig();
    }

//    @Before
//    public void beforeTest() {
//        infoflow.getSources() = new ArrayList<>();
//        infoflow.getSources().add("<edu.cmu.cs.mvelezce.analysis.Source: boolean getOptionA()>");
//        infoflow.getSources().add("<edu.cmu.cs.mvelezce.analysis.Source: boolean getOptionB()>");
//
//        infoflow.getSinks() = new ArrayList<>();
//        infoflow.getSinks().add("<edu.cmu.cs.mvelezce.analysis.Sink: boolean getDecision(boolean)>");
//    }

    protected void checkInfoflow(TaintInfoflow infoflow, int resultCount) {
        if(infoflow.isResultAvailable()) {
            InfoflowResults map = infoflow.getResults();
            boolean containsSink = false;
            List<String> actualSinkStrings = new LinkedList<String>();
            assertEquals(resultCount, map.size());
            for(String sink : infoflow.getSinks()) {
                if(map.containsSinkMethod(sink)) {
                    containsSink = true;
                    actualSinkStrings.add(sink);
                }
            }

            assertTrue(containsSink);
            boolean onePathFound = false;
            for(String sink : actualSinkStrings) {
                boolean hasPath = false;
                for(String source : infoflow.getSources()) {
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

    protected void negativeCheckInfoflow(TaintInfoflow infoflow) {
        if(infoflow.isResultAvailable()) {
            InfoflowResults map = infoflow.getResults();
            for(String sink : infoflow.getSinks()) {
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
    public void basic0Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic0");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add("<edu.cmu.cs.mvelezce.taint.programs.Basic0: void main(java.lang.String[])>");

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.negativeCheckInfoflow(infoflow);
    }

    @Test
    public void basic1Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic1");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add("<edu.cmu.cs.mvelezce.taint.programs.Basic1: void main(java.lang.String[])>");

        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 2);
        infoflow.checkResults();
    }

    @Test
    public void basic2Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic2");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add("<edu.cmu.cs.mvelezce.taint.programs.Basic2: void main(java.lang.String[])>");

        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 2);
        infoflow.checkResults();
    }

    @Test
    public void basic3Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic3");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add("<edu.cmu.cs.mvelezce.taint.programs.Basic3: void main(java.lang.String[])>");

        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 2);
        infoflow.checkResults();
    }

    @Test
    public void basic4Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic4");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add("<edu.cmu.cs.mvelezce.taint.programs.Basic4: void main(java.lang.String[])>");

        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 1);
        infoflow.checkResults();
    }

    @Test
    public void basic5Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic5");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add("<edu.cmu.cs.mvelezce.taint.programs.Basic5: void main(java.lang.String[])>");

        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 1);
        infoflow.checkResults();
    }

    @Test
    public void basic6Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic6");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add("<edu.cmu.cs.mvelezce.taint.programs.Basic6: void main(java.lang.String[])>");

        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 1);
        infoflow.checkResults();
    }

    @Test
    public void basic7Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic7");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add("<edu.cmu.cs.mvelezce.taint.programs.Basic7: void main(java.lang.String[])>");

        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 3);
        infoflow.checkResults();
    }

    @Test
    public void basic8Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic8");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add("<edu.cmu.cs.mvelezce.taint.programs.Basic8: void main(java.lang.String[])>");

        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.negativeCheckInfoflow(infoflow);
    }

    @Test
    public void basic9Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic9");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add("<edu.cmu.cs.mvelezce.taint.programs.Basic9: void main(java.lang.String[])>");

        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 3);
        infoflow.checkResults();
    }

    @Test
    public void Sleep0Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("sleep0");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add("<edu.cmu.cs.mvelezce.taint.programs.Sleep0: void main(java.lang.String[])>");

        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 6);
        infoflow.checkResults();
    }

    @Test
    public void Union0Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("union0");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add("<edu.cmu.cs.mvelezce.taint.programs.Union0: void main(java.lang.String[])>");

        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 4);
        infoflow.checkResults();
    }

    @Test
    public void Union2Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("union2");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add("<edu.cmu.cs.mvelezce.taint.programs.Union2: void main(java.lang.String[])>");

        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 5);
        infoflow.checkResults();
    }

    @Test
    public void pngtasticTest() throws IOException {
        File pngtastic = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/pngtastic/target/classes");
        AnalysisTest.appPath += AnalysisTest.sep + pngtastic;

        TaintInfoflow infoflow = new TaintInfoflow("pngtastic");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add("<com.googlecode.pngtastic.PngtasticColorCounter: void main(java.lang.String[])>");

        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 3);
        infoflow.checkResults();
    }
}
