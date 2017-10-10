package edu.cmu.cs.mvelezce.taint;

import edu.cmu.cs.mvelezce.analysis.TaintInfoflow;
import edu.cmu.cs.mvelezce.soot.SootConfig;
import edu.cmu.cs.mvelezce.visualization.HTMLOutputGenerator;
import edu.cmu.cs.mvelezce.visualization.HTMLPathGenerator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import soot.MethodOrMethodContext;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.config.IInfoflowConfig;
import soot.jimple.infoflow.data.pathBuilders.DefaultPathBuilderFactory;
import soot.jimple.infoflow.problems.InfoflowProblem;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.util.dot.DotGraph;
import soot.util.queue.QueueReader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

public class AnalysisTest {

    protected static final String sep = System.getProperty("path.separator");
    protected static String appPath;
    protected static String libPath;
    //    protected static List<String> sources;
//    protected static List<String> sinks;
    protected static InfoflowConfiguration infoflowConfiguration;
    protected static IInfoflowConfig sootConfiguration;

    @Before
    public void resetSootAndStream() throws IOException {
        soot.G.reset();
        System.gc();

    }

    @Before
    public void setUp() throws IOException {
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
        // More precise call graph algorithm
        AnalysisTest.infoflowConfiguration.setCallgraphAlgorithm(InfoflowConfiguration.CallgraphAlgorithm.SPARK);
        AnalysisTest.infoflowConfiguration.setEnableImplicitFlows(true);

        AnalysisTest.infoflowConfiguration.setCodeEliminationMode(InfoflowConfiguration.CodeEliminationMode.NoCodeElimination);
        AnalysisTest.infoflowConfiguration.setAccessPathLength(10);
        AnalysisTest.infoflowConfiguration.setDataFlowSolver(InfoflowConfiguration.DataFlowSolver.ContextFlowSensitive);
        AnalysisTest.infoflowConfiguration.setMaxThreadNum(1);

        AnalysisTest.infoflowConfiguration.setInspectSources(true);
        AnalysisTest.infoflowConfiguration.setInspectSinks(false);

        AnalysisTest.infoflowConfiguration.setAliasingAlgorithm(InfoflowConfiguration.AliasingAlgorithm.None);
        AnalysisTest.infoflowConfiguration.setFlowSensitiveAliasing(false);
        AnalysisTest.infoflowConfiguration.setStopAfterFirstFlow(false);
        AnalysisTest.infoflowConfiguration.setEnableStaticFieldTracking(true);
        AnalysisTest.infoflowConfiguration.setEnableExceptionTracking(false);
        AnalysisTest.infoflowConfiguration.setOneSourceAtATime(true);

        AnalysisTest.infoflowConfiguration.setSingleJoinPointAbstraction(true);
//        AnalysisTest.infoflowConfiguration.setEnableArraySizeTainting(false);
//        AnalysisTest.infoflowConfiguration.setEnableTypeChecking(false);
//        InfoflowConfiguration.setMergeNeighbors(true);

//        AnalysisTest.infoflowConfiguration.setSequentialPathProcessing(true);
//        AnalysisTest.infoflowConfiguration.setDataFlowTimeout(900);

        // Incorrect results
//        AnalysisTest.infoflowConfiguration.setIgnoreFlowsInSystemPackages(true);
//        AnalysisTest.infoflowConfiguration.setExcludeSootLibraryClasses(true);


//        AnalysisTest.infoflowConfiguration.setStopAfterFirstKFlows(10);

        // Config soot
        AnalysisTest.sootConfiguration = new SootConfig();
    }

    @Test
    public void interaction0_1() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String systemName = "interaction0_1";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Interaction0_1: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(1);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "src/test/java/edu/cmu/cs/mvelezce/taint/programs";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
    }

    @Test
    public void interaction0() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String systemName = "interaction0";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Interaction0: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(3);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "src/test/java/edu/cmu/cs/mvelezce/taint/programs";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
    }

    @Test
    public void interaction1() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String systemName = "interaction1";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Interaction1: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(5);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "src/test/java/edu/cmu/cs/mvelezce/taint/programs";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
    }

    @Test
    public void interaction2() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String systemName = "interaction2";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Interaction2: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(2);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "src/test/java/edu/cmu/cs/mvelezce/taint/programs";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
    }

    @Test
    public void interaction3() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String systemName = "interaction3";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Interaction3: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(2);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "src/test/java/edu/cmu/cs/mvelezce/taint/programs";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
    }

    @Test
    public void interaction4() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String systemName = "interaction4";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Interaction4: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(2);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "src/test/java/edu/cmu/cs/mvelezce/taint/programs";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
    }

    @Test
    public void interaction5() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String systemName = "interaction5";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Interaction5: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(2);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "src/test/java/edu/cmu/cs/mvelezce/taint/programs";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
    }

    @Test
    public void interaction5_0() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String systemName = "interaction5_0";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Interaction5_0: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(3);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "src/test/java/edu/cmu/cs/mvelezce/taint/programs";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
    }

    @Test
    public void interaction5_1() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String systemName = "interaction5_1";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Interaction5_1: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(2);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "src/test/java/edu/cmu/cs/mvelezce/taint/programs";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
    }

    @Test
    public void interaction5_2() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String systemName = "interaction5_2";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Interaction5_2: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(3);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "src/test/java/edu/cmu/cs/mvelezce/taint/programs";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
    }

    @Test
    public void interaction5_3() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String systemName = "interaction5_3";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Interaction5_3: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(6);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "src/test/java/edu/cmu/cs/mvelezce/taint/programs";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
    }

    @Test
    public void interaction5_4() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String systemName = "interaction5_4";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Interaction5_4: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(3);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "src/test/java/edu/cmu/cs/mvelezce/taint/programs";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
    }

    @Test
    public void interaction5_5() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String systemName = "interaction5_5";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Interaction5_5: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(6);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "src/test/java/edu/cmu/cs/mvelezce/taint/programs";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
    }

    @Test
    public void interaction5_6() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String systemName = "interaction5_6";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Interaction5_6: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(2);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "src/test/java/edu/cmu/cs/mvelezce/taint/programs";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }

    @Test
    public void Interaction5_ColorCounter() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String systemName = "interaction5_ColorCounter";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Interaction5_ColorCounter: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(5);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "src/test/java/edu/cmu/cs/mvelezce/taint/programs";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }

    @Test
    public void Interaction5_ColorCounter_1() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String systemName = "interaction5_ColorCounter_1";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Interaction5_ColorCounter_1: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(2);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "src/test/java/edu/cmu/cs/mvelezce/taint/programs";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }

    @Test
    public void interaction6() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String systemName = "interaction6";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Interaction6: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(3);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "src/test/java/edu/cmu/cs/mvelezce/taint/programs";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }

    @Test
    public void interaction7() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String systemName = "interaction7";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Interaction7: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(2);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "src/test/java/edu/cmu/cs/mvelezce/taint/programs";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }

    @Test
    public void interaction8() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String systemName = "interaction8";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Interaction8: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(1);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "src/test/java/edu/cmu/cs/mvelezce/taint/programs";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }

    @Test
    public void interaction8_0() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String systemName = "interaction8_0";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Interaction8_0: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(7);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "src/test/java/edu/cmu/cs/mvelezce/taint/programs";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }

    @Test
    public void interaction9() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String systemName = "interaction9";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Interaction9: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(3);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "src/test/java/edu/cmu/cs/mvelezce/taint/programs";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }

    @Test
    public void interaction10() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String systemName = "interaction10";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Interaction10: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(2);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "src/test/java/edu/cmu/cs/mvelezce/taint/programs";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }

    @Test
    public void basic0Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic0");

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic0: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(1);
        infoflow.saveJimpleFiles();
    }

    @Test
    public void basic1Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic1");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic1: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        infoflow.checkResults();
        infoflow.saveJimpleFiles();
    }

    @Test
    public void basic2Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic2");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));


        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic2: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        infoflow.checkResults();
    }

    @Test
    public void basic3Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic3");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));


        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic3: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        infoflow.checkResults();
    }

    @Test
    public void basic4Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic4");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));


        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic4: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        infoflow.checkResults();
    }

    @Test
    public void basic5Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic5");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));


        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic5: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        infoflow.checkResults();
    }

    @Test
    public void basic6Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic6");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));


        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic6: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        infoflow.checkResults();
    }

    @Test
    public void basic7Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic7");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));


        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic7: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        infoflow.checkResults();
    }

    @Test
    public void basic8Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic8");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));


        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic8: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
    }

    @Test
    public void basic9Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic9");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));


        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic9: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        infoflow.checkResults();
    }

    @Test
    public void basic10Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic10");

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic10: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(1);
        infoflow.saveJimpleFiles();
    }

    @Test
    public void basic11Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic11");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic11: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        infoflow.checkResults();
    }

    @Test
    public void basic12Test1() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic12");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic12: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        infoflow.checkResults();
    }

    @Test
    public void basic12Test2() throws IOException {
        AnalysisTest.infoflowConfiguration.setEnableExceptionTracking(false);

        TaintInfoflow infoflow = new TaintInfoflow("basic12");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));


        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic12: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        infoflow.checkResults();
    }

    @Test
    public void basic13Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic13");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic13: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        infoflow.checkResults();
    }

    @Test
    public void basic14Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic14");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic14: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        infoflow.checkResults();
    }

    @Test
    public void basic15Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic15");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic15: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        infoflow.checkResults();
    }

    @Test
    public void basic16Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic16");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic16: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        infoflow.checkResults();
    }

    @Test
    public void basic17Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic17");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic17: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        infoflow.checkResults();
    }

    @Test
    public void basic18Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic18");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic18: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        infoflow.checkResults();
    }

    @Test
    public void basic19Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("basic19");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Basic19: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        infoflow.checkResults();
    }

    @Test
    public void problem1Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("problem1");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));


        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Problem1: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        infoflow.saveJimpleFiles();
        infoflow.checkResults();
    }

    @Test
    public void problem2Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("problem2");

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Problem2: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(1);
        infoflow.saveJimpleFiles();
    }

    @Test
    public void problem3Test() throws IOException {
        AnalysisTest.infoflowConfiguration.setEnableExceptionTracking(true);

        TaintInfoflow infoflow = new TaintInfoflow("problem3");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Problem3: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        infoflow.checkResults();
    }

    @Test
    public void problem4Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("problem4");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Problem4: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        infoflow.checkResults();
    }

    @Test
    public void problem5Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("problem5");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
////        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, true));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Problem5: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        infoflow.checkResults();
    }

    @Test
    public void problem6Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("problem6");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
////        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, true));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Problem6: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        infoflow.checkResults();
    }

    @Test
    public void problem7Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("problem7");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Problem7: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        infoflow.checkResults();
    }

    @Test
    public void problem8Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("problem8");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
////        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, true));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Problem8: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        infoflow.checkResults();
    }

    @Test
    public void problem9Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("problem9");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
////        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, true));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Problem9: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        infoflow.checkResults();
    }

    @Test
    public void problem10Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("problem10");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);

//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Problem10: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        infoflow.checkResults();
    }

    @Test
    public void problem11Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("problem11");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Problem11: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        infoflow.checkResults();
    }

    @Test
    public void Sleep0Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("sleep0");

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Sleep0: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(1);
        infoflow.saveJimpleFiles();
    }

    @Test
    public void Sleep1Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("sleep1");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));


        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Sleep1: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        infoflow.checkResults();
    }

    @Test
    public void Union0Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("union0");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Union0: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        infoflow.checkResults();
    }

    @Test
    public void Union2Test() throws IOException {
        TaintInfoflow infoflow = new TaintInfoflow("union2");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

        String entryPoint = "<edu.cmu.cs.mvelezce.taint.programs.Union2: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        infoflow.checkResults();
    }

    @Test
    public void pngtasticColorCounterTest() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/pngtastic-counter/out/production/pngtastic-counter");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        String systemName = "pngtasticColorCounter";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<counter.com.googlecode.pngtastic.Run: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(36);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/pngtastic-counter/src";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
    }

    @Test
    public void pngtasticOptimizerTest() throws IOException {
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/pngtastic-optimizer/out/production/pngtastic-optimizer");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        String systemName = "pngtasticOptimizer";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<optimizer.com.googlecode.pngtastic.Run: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(398);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/pngtastic-optimizer/src";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }

    @Test
    public void elevatorTest() throws IOException {
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/elevator/out/production/elevator");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        TaintInfoflow infoflow = new TaintInfoflow("elevator");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        String entryPoint = "<edu.cmu.cs.mvelezce.PL_Interface_impl: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(1);
        infoflow.saveJimpleFiles();
    }

    @Test
    public void zipmeTest() throws IOException {
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/zipme/out/production/zipme");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        TaintInfoflow infoflow = new TaintInfoflow("zipme");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        String entryPoint = "<edu.cmu.cs.mvelezce.zip.ZipMain: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(1);
        infoflow.saveJimpleFiles();
    }

    @Test
    public void findTest() throws IOException {
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/unix4j/out/production/unix4j");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        TaintInfoflow infoflow = new TaintInfoflow("find");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

        String entryPoint = "<edu.cmu.cs.mvelezce.FindMain: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        infoflow.checkResults();
    }

    @Test
    public void runningExample() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/running-example/target/classes");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        String systemName = "running-example";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.Example: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(4);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/running-example/src/main/java";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }

    @Test
    public void prevaylerTest() throws IOException {
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/prevayler/target/classes");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        String systemName = "prevayler";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<org.prevayler.demos.demo1.PrimeNumbers: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(77);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

//        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/prevayler/src/main/java";
//
//        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
//        generator.generateHTMLPage();
//        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }

    @Test
    public void berkeleyDBTest() throws IOException {
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/berkeleydb/target/classes");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

//        file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/berkeley-db/out/test/berkeley-db");
//        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        String systemName = "berkeley-db";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<berkeley.persist.gettingStarted.ExampleDatabasePut: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(7088);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/berkeleydb/src/main/java";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }

    @Test
    public void encryptionTest() throws IOException {
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/encryption/out/production/Encryption");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        TaintInfoflow infoflow = new TaintInfoflow("encryption");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        String entryPoint = "<edu.cmu.cs.mvelezce.EncryptionMain: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        infoflow.checkResults();
    }

    @Test
    public void jarchivelibTest() throws IOException {
//        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/jarchivelib/target/classes");
//        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/jarchivelib/commons-compress/target/classes");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        TaintInfoflow infoflow = new TaintInfoflow("jarchivelib");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        String entryPoint = "<edu.cmu.cs.mvelezce.Jarchivelib: void main(java.lang.String[])>";
        String entryPoint = "<edu.cmu.cs.mvelezce.analysis.option.CCMain: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        infoflow.checkResults();
    }

    @Test
    public void commonsCompressTest() throws IOException {
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/commons-compress/target/classes");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        String systemName = "commonsCompress";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.CCMain: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(420);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/commons-compress/src/main/java";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
    }

    @Test
    public void javalameTest() throws IOException {
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/java-lame/out/production/classes");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        TaintInfoflow infoflow = new TaintInfoflow("javalame");
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);

//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));

//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        String entryPoint = "<edu.cmu.cs.mvelezce.LameMain: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());
        infoflow.checkResults();
    }

    @Test
    public void kanzi() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/kanzi/target/classes");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        String systemName = "kanzi";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<kanzi.Run: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(141);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/kanzi/src";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
    }

    @Test
    public void regions() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        String systemName = "regions";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.Regions: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(4);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/src";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
//        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }

    @Test
    public void regions0() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        String systemName = "regions0";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.Regions0: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(5);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/src";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }

    @Test
    public void regions1() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        String systemName = "regions1";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.Regions1: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(4);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/src";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }

    @Test
    public void regions2() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        String systemName = "regions2";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.Regions2: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(3);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/src";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }

    @Test
    public void regions3() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        String systemName = "regions3";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.Regions3: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(4);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/src";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }

    @Test
    public void regions4() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        String systemName = "regions4";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.Regions4: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(4);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/src";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }

    @Test
    public void regions5() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        String systemName = "regions5";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.Regions5: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(3);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/src";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }

    @Test
    public void regions6() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        String systemName = "regions6";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.Regions6: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(4);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/src";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }

    @Test
    public void regions7() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        String systemName = "regions7";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.Regions7: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(4);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/src";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }

    @Test
    public void regions8() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        String systemName = "regions8";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.Regions8: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(3);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/src";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
//        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }

    @Test
    public void regions9() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        String systemName = "regions9";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.Regions9: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(5);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/src";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
//        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }

    @Test
    public void regions10() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        String systemName = "regions10";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.Regions10: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(3);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/src";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
//        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }

    @Test
    public void regions11() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        String systemName = "regions11";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.Regions11: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(1);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/src";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }

    @Test
    public void propertiesObject() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        String systemName = "properties-object";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.PropertiesObject: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(1);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/src";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }

    @Test
    public void propertiesObject1() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        String systemName = "properties-object1";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.PropertiesObject1: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(1);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/src";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }

    @Test
    public void interactions() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        String systemName = "interactions";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.Interactions: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(1);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/src";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }

    @Test
    public void interactions1() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy");
        AnalysisTest.appPath = file + AnalysisTest.sep + AnalysisTest.appPath;

        String systemName = "interactions1";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(AnalysisTest.infoflowConfiguration);
        infoflow.setSootConfig(AnalysisTest.sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, false));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<edu.cmu.cs.mvelezce.Interactions1: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(AnalysisTest.appPath, AnalysisTest.libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(AnalysisTest.appPath, AnalysisTest.libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(1);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();

        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/src";

        HTMLOutputGenerator generator = new HTMLOutputGenerator(root, systemName);
        generator.generateHTMLPage();
        HTMLPathGenerator.generateHTMLForSystem(root, systemName);
    }

}

