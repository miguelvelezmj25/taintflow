package edu.cmu.cs.mvelezce.taint;

import edu.cmu.cs.mvelezce.analysis.TaintInfoflow;
import edu.cmu.cs.mvelezce.soot.SootConfig;
import org.junit.BeforeClass;
import org.junit.Test;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.config.IInfoflowConfig;
import soot.jimple.infoflow.data.pathBuilders.DefaultPathBuilderFactory;
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

//        f = new File("./soot-infoflow/");
//        File testSrc3 = new File(f, "bin");

        if(!(testSrc1.exists() || testSrc2.exists()/* || testSrc3.exists()*/)) {
            fail("Test aborted - none of the test sources are available");
        }

        AnalysisTest.appPath = testSrc1.getCanonicalPath() + AnalysisTest.sep + testSrc2.getCanonicalPath();//+ AnalysisTest.sep + testSrc3.getCanonicalPath();
        libPath = System.getProperty("java.home") + File.separator + "lib" + File.separator + "rt.jar"
                + AnalysisTest.sep + System.getProperty("java.home") + File.separator + "lib" + File.separator + "jce.jar";

        // Config information flow
        AnalysisTest.infoflowConfiguration = new InfoflowConfiguration();
        AnalysisTest.infoflowConfiguration.setCallgraphAlgorithm(InfoflowConfiguration.CallgraphAlgorithm.CHA);
//        AnalysisTest.infoflowConfiguration.setCallgraphAlgorithm(InfoflowConfiguration.CallgraphAlgorithm.SPARK);
        AnalysisTest.infoflowConfiguration.setEnableImplicitFlows(true);
        AnalysisTest.infoflowConfiguration.setCodeEliminationMode(InfoflowConfiguration.CodeEliminationMode.NoCodeElimination);
        AnalysisTest.infoflowConfiguration.setInspectSinks(false);
        AnalysisTest.infoflowConfiguration.setAccessPathLength(10_000);
//        AnalysisTest.infoflowConfiguration.setAccessPathLength(1);
        AnalysisTest.infoflowConfiguration.setDataFlowSolver(InfoflowConfiguration.DataFlowSolver.ContextFlowSensitive);
        AnalysisTest.infoflowConfiguration.setAliasingAlgorithm(InfoflowConfiguration.AliasingAlgorithm.FlowSensitive);
        AnalysisTest.infoflowConfiguration.setStopAfterFirstFlow(false);
        AnalysisTest.infoflowConfiguration.setEnableStaticFieldTracking(true);
        AnalysisTest.infoflowConfiguration.setEnableExceptionTracking(true);

//        AnalysisTest.infoflowConfiguration.setWriteOutputFiles(true);


//        AnalysisTest.infoflowConfiguration.setUseThisChainReduction(true);
//        AnalysisTest.infoflowConfiguration.setUseRecursiveAccessPaths(true);
//        AnalysisTest.infoflowConfiguration.setExcludeSootLibraryClasses(false);

//        InfoflowConfiguration.setPathAgnosticResults(true);


//        AnalysisTest.infoflowConfiguration.setEnableExceptionTracking(true);


//        InfoflowConfiguration.setOneResultPerAccessPath(true);

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

    protected void printResultCount(TaintInfoflow infoflow) {
        System.out.println("Number of results: " + infoflow.getResults().size());
    }

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
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic0: void main(java.lang.String[])>";

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.negativeCheckInfoflow(infoflow);
    }

    @Test
    public void basic1Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic1");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic1: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 2);
        infoflow.checkResults();
        infoflow.saveJimpleFiles();
    }

    @Test
    public void basic2Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic2");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);


        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic2: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 2);
        infoflow.checkResults();
    }

    @Test
    public void basic3Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic3");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);


        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic3: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 2);
        infoflow.checkResults();
    }

    @Test
    public void basic4Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic4");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);


        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic4: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 1);
        infoflow.checkResults();
    }

    @Test
    public void basic5Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic5");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);


        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic5: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 1);
        infoflow.checkResults();
    }

    @Test
    public void basic6Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic6");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);


        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic6: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 1);
        infoflow.checkResults();
    }

    @Test
    public void basic7Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic7");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);


        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic7: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 3);
        infoflow.checkResults();
    }

    @Test
    public void basic8Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic8");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);


        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic8: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.negativeCheckInfoflow(infoflow);
    }

    @Test
    public void basic9Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic9");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);


        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic9: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 3);
        infoflow.checkResults();
    }

    @Test
    public void basic10Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic10");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);


        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic10: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 4);
        infoflow.checkResults();
    }

    @Test
    public void basic11Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic11");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic11: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 4);
        this.printResultCount(infoflow);
        infoflow.checkResults();
    }

    @Test
    public void basic12Test1() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic12");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic12: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 10);
        this.printResultCount(infoflow);
        infoflow.checkResults();
    }

    @Test
    public void basic12Test2() throws IOException {
        AnalysisTest.infoflowConfiguration.setEnableExceptionTracking(false);

        TaintInfoflow infoflow = new TaintInfoflow("basic12");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);


        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic12: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 2);
        this.printResultCount(infoflow);
        infoflow.checkResults();
    }

    @Test
    public void basic13Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic13");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic13: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 7);
        infoflow.checkResults();
    }

    @Test
    public void basic14Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic14");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic14: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 7);
        infoflow.checkResults();
    }

    @Test
    public void basic15Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic15");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic15: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 1);
        infoflow.checkResults();
    }

    @Test
    public void basic16Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic16");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic16: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 2);
        infoflow.checkResults();
    }

    @Test
    public void basic17Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic17");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic17: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 3);
        infoflow.checkResults();
    }

    @Test
    public void basic18Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic18");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic18: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 3);
        infoflow.checkResults();
    }

    @Test
    public void basic19Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic19");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic19: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 3);
        infoflow.checkResults();
    }

    @Test
    public void problem1Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("problem1");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Problem1: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        infoflow.saveJimpleFiles();
        this.checkInfoflow(infoflow, 3);
        this.printResultCount(infoflow);
        infoflow.checkResults();
    }

    @Test
    public void problem2Test() throws IOException {
        AnalysisTest.infoflowConfiguration.setEnableExceptionTracking(true);

        TaintInfoflow infoflow = new TaintInfoflow("problem2");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Problem2: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 3);
        this.printResultCount(infoflow);
        infoflow.checkResults();
    }

    @Test
    public void problem3Test() throws IOException {
        AnalysisTest.infoflowConfiguration.setEnableExceptionTracking(true);

        TaintInfoflow infoflow = new TaintInfoflow("problem3");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Problem3: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 3);
        this.printResultCount(infoflow);
        infoflow.checkResults();
    }

    @Test
    public void problem4Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("problem4");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Problem4: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 3);
        this.printResultCount(infoflow);
        infoflow.checkResults();
    }

    @Test
    public void problem5Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("problem5");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, true));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Problem5: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 3);
        this.printResultCount(infoflow);
        infoflow.checkResults();
    }

    @Test
    public void problem6Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("problem6");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, true));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Problem6: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
//        this.checkInfoflow(infoflow, 3);
        this.printResultCount(infoflow);
        infoflow.checkResults();
    }

    @Test
    public void Sleep0Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("sleep0");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);


        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Sleep0: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 6);
        infoflow.checkResults();
    }

    @Test
    public void Sleep1Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("sleep1");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);


        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Sleep1: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 6);
        infoflow.checkResults();
    }

    @Test
    public void Union0Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("union0");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);


        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Union0: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 4);
        infoflow.checkResults();
    }

    @Test
    public void Union2Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("union2");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Union2: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 5);
        infoflow.checkResults();
    }

    @Test
    public void pngtasticColorCounterTest() throws IOException {
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/pngtastic/target/classes");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        TaintInfoflow infoflow = new TaintInfoflow("pngtasticColorCounter");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, true));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        String entryPoint = "<com.googlecode.pngtastic.PngtasticColorCounter: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
//        this.checkInfoflow(infoflow, 19);
        infoflow.checkResults();
        infoflow.saveJimpleFiles();
    }

    @Test
    public void pngtasticOptimizerTest() throws IOException {
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/pngtastic-optimizer/out/production/pngtastic-optimizer");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        TaintInfoflow infoflow = new TaintInfoflow("pngtasticOptimizer");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, true));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, true));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        String entryPoint = "<com.googlecode.pngtastic.Run: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
//        this.checkInfoflow(infoflow, 13);
        infoflow.checkResults();
        infoflow.saveJimpleFiles();
    }

    @Test
    public void elevatorTest() throws IOException {
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/elevator/out/production/elevator");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        TaintInfoflow infoflow = new TaintInfoflow("elevator");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        String entryPoint = "<edu.cmu.cs.mvelezce.PL_Interface_impl: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 45);
        infoflow.checkResults();
    }

    @Test
    public void zipmeTest() throws IOException {
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/zipme/out/production/zipme");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        TaintInfoflow infoflow = new TaintInfoflow("zipme");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        String entryPoint = "<edu.cmu.cs.mvelezce.zip.ZipMain: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 9);
        infoflow.checkResults();
    }

    @Test
    public void findTest() throws IOException {
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/unix4j/out/production/unix4j");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        TaintInfoflow infoflow = new TaintInfoflow("find");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        String entryPoint = "<edu.cmu.cs.mvelezce.FindMain: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 9);
        infoflow.checkResults();
    }

    @Test
    public void runningExampleTest() throws IOException {
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/running-example/target/classes");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        TaintInfoflow infoflow = new TaintInfoflow("running-example");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);


        String entryPoint = "<edu.cmu.cs.mvelezce.Example: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 3);
        infoflow.checkResults();
    }

    @Test
    public void prevaylerTest() throws IOException {
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/prevayler/demos/demo1/target/classes");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/prevayler/factory/target/classes");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/prevayler/core/target/classes");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        TaintInfoflow infoflow = new TaintInfoflow("prevayler");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);


        String entryPoint = "<org.prevayler.demos.demo1.PrimeNumbers: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 4);
        infoflow.checkResults();
    }

    @Test
    public void encryptionTest() throws IOException {
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/encryption/out/production/Encryption");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        TaintInfoflow infoflow = new TaintInfoflow("encryption");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        String entryPoint = "<edu.cmu.cs.mvelezce.EncryptionMain: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
//        this.checkInfoflow(infoflow, 4);
        infoflow.checkResults();
    }

    @Test
    public void jarchivelibTest() throws IOException {
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/jarchivelib/target/classes");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        TaintInfoflow infoflow = new TaintInfoflow("jarchivelib");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        String entryPoint = "<edu.cmu.cs.mvelezce.Jarchivelib: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
//        this.checkInfoflow(infoflow, 4);
        infoflow.checkResults();
    }

    @Test
    public void javalameTest() throws IOException {
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/java-lame/out/production/classes");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        TaintInfoflow infoflow = new TaintInfoflow("javalame");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, true));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        String entryPoint = "<edu.cmu.cs.mvelezce.Lame: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
//        this.checkInfoflow(infoflow, 4);
        infoflow.checkResults();
    }

    @Test
    public void kanziTest() throws IOException {
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/kanzi/out/production/kanzi");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        TaintInfoflow infoflow = new TaintInfoflow("kanzi");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        String entryPoint = "<edu.cmu.cs.mvelezce.Kanzi: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        this.checkInfoflow(infoflow, 4);
        infoflow.checkResults();
    }
}
