package edu.cmu.cs.mvelezce.taint;

import edu.cmu.cs.mvelezce.soot.SootConfig;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import soot.jimple.Stmt;
import soot.jimple.infoflow.Infoflow;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.config.IInfoflowConfig;
import soot.jimple.infoflow.results.InfoflowResults;
import soot.jimple.infoflow.results.ResultSinkInfo;
import soot.tagkit.BytecodeOffsetTag;
import soot.tagkit.Tag;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class AnalysisTest {

    protected static String appPath;
    protected static String libPath;
    protected static List<String> sources;
    protected static List<String> sinks;
    protected static InfoflowConfiguration infoflowConfiguration;
    protected static IInfoflowConfig sootConfiguration;

    @BeforeClass
    public static void setUp() throws IOException {
        final String sep = System.getProperty("path.separator");
        File f = new File(".");
        File testSrc1 = new File(f, "target" + File.separator + "classes");
        File testSrc2 = new File(f, "target" + File.separator + "test-classes");

        if (!(testSrc1.exists() || testSrc2.exists())) {
            fail("Test aborted - none of the test sources are available");
        }
        AnalysisTest.appPath = testSrc1.getCanonicalPath() + sep + testSrc2.getCanonicalPath();
        AnalysisTest.libPath = System.getProperty("java.home") + File.separator + "lib" + File.separator + "rt.jar";

        // Config information flow
        AnalysisTest.infoflowConfiguration = new InfoflowConfiguration();
        AnalysisTest.infoflowConfiguration.setCallgraphAlgorithm(InfoflowConfiguration.CallgraphAlgorithm.CHA);
        AnalysisTest.infoflowConfiguration.setEnableImplicitFlows(true);
        AnalysisTest.infoflowConfiguration.setCodeEliminationMode(InfoflowConfiguration.CodeEliminationMode.NoCodeElimination);
        AnalysisTest.infoflowConfiguration.setInspectSinks(true);

        // Config soot
        AnalysisTest.sootConfiguration = new SootConfig();
    }

    @Before
    public void beforeTest() {
        AnalysisTest.sources = new ArrayList<>();
        AnalysisTest.sources.add("<edu.cmu.cs.mvelezce.analysis.Options: boolean getOption()>");

        AnalysisTest.sinks = new ArrayList<>();
        AnalysisTest.sinks.add("<edu.cmu.cs.mvelezce.analysis.Options: boolean getDecision(boolean)>");
    }

    protected void checkInfoflow(Infoflow infoflow, int resultCount) {
        if(infoflow.isResultAvailable()) {
            InfoflowResults map = infoflow.getResults();
            boolean containsSink = false;
            List<String> actualSinkStrings = new LinkedList<String>();
            assertEquals(resultCount, map.size());
            for(String sink : AnalysisTest.sinks) {
                if(map.containsSinkMethod(sink)) {
                    containsSink = true;
                    actualSinkStrings.add(sink);
                }
            }

            assertTrue(containsSink);
            boolean onePathFound = false;
            for(String sink : actualSinkStrings) {
                boolean hasPath = false;
                for(String source : AnalysisTest.sources) {
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

            for(ResultSinkInfo resultSinkInfo : map.getResults().keySet()) {
                this.printSinkResult(resultSinkInfo);
            }

        }
        else {
            fail("result is not available");
        }
    }

    protected void negativeCheckInfoflow(Infoflow infoflow) {
        if(infoflow.isResultAvailable()) {
            InfoflowResults map = infoflow.getResults();
            for(String sink : AnalysisTest.sinks) {
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

    protected void printSinkResult(ResultSinkInfo resultSinkInfo) {
        System.out.println("Result sink info: " + resultSinkInfo);

        Stmt sink = resultSinkInfo.getSink();
        System.out.println("Sink: " + sink);
        System.out.println("Sink: " + sink);


        List<Integer> bytecodeIndexes = new ArrayList<>();

        for(Tag tag : resultSinkInfo.getSink().getTags()) {
            if(tag instanceof BytecodeOffsetTag) {
                int bytecodeIndex = ((BytecodeOffsetTag) tag).getBytecodeOffset();
                bytecodeIndexes.add(bytecodeIndex);
            }
        }

        if(bytecodeIndexes.isEmpty()) {
            bytecodeIndexes.add(-1);
        }

        System.out.println("Bytecode indexes: " + bytecodeIndexes);
    }

    @Test
    public void basic0Test() throws IOException {
        Infoflow infoflow = new Infoflow();
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add("<edu.cmu.cs.mvelezce.taint.programs.Basic0: void main(java.lang.String[])>");

        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, AnalysisTest.sources, AnalysisTest.sinks);
        this.checkInfoflow(infoflow, 1);
    }

    @Test
    public void basic1Test() throws IOException {
        Infoflow infoflow = new Infoflow();
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add("<edu.cmu.cs.mvelezce.taint.programs.Basic1: void main(java.lang.String[])>");

        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, AnalysisTest.sources, AnalysisTest.sinks);
        this.checkInfoflow(infoflow, 2);
    }

    @Test
    public void basic2Test() throws IOException {
        Infoflow infoflow = new Infoflow();
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add("<edu.cmu.cs.mvelezce.taint.programs.Basic2: void main(java.lang.String[])>");

        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, AnalysisTest.sources, AnalysisTest.sinks);
        this.checkInfoflow(infoflow, 2);
    }

    @Test
    public void basic3Test() throws IOException {
        Infoflow infoflow = new Infoflow();
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add("<edu.cmu.cs.mvelezce.taint.programs.Basic3: void main(java.lang.String[])>");

        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, AnalysisTest.sources, AnalysisTest.sinks);
        this.checkInfoflow(infoflow, 2);
    }

    @Test
    public void basic4Test() throws IOException {
        Infoflow infoflow = new Infoflow();
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add("<edu.cmu.cs.mvelezce.taint.programs.Basic4: void main(java.lang.String[])>");

        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, AnalysisTest.sources, AnalysisTest.sinks);
        this.checkInfoflow(infoflow, 1);
    }

    @Test
    public void basic5Test() throws IOException {
        Infoflow infoflow = new Infoflow();
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add("<edu.cmu.cs.mvelezce.taint.programs.Basic5: void main(java.lang.String[])>");

        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, AnalysisTest.sources, AnalysisTest.sinks);
        this.checkInfoflow(infoflow, 1);
    }

    @Test
    public void basic6Test() throws IOException {
        Infoflow infoflow = new Infoflow();
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add("<edu.cmu.cs.mvelezce.taint.programs.Basic6: void main(java.lang.String[])>");

        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, AnalysisTest.sources, AnalysisTest.sinks);
        this.checkInfoflow(infoflow, 1);
    }

    @Test
    public void basic7Test() throws IOException {
        Infoflow infoflow = new Infoflow();
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/soot-infoflow/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add("<edu.cmu.cs.mvelezce.taint.programs.Basic7: void main(java.lang.String[])>");

        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, AnalysisTest.sources, AnalysisTest.sinks);
        this.checkInfoflow(infoflow, 3);
    }

}
