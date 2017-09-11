package edu.cmu.cs.mvelezce.analysis;

import edu.cmu.cs.mvelezce.soot.SootConfig;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.config.IInfoflowConfig;
import soot.jimple.infoflow.data.pathBuilders.DefaultPathBuilderFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Run {
    /*
     java -Xms300G -Xmx300G -cp ./target/classes:./soot-infoflow/bin:./heros/slf4j-api-1.7.5.jar:./soot/classes:./soot/target/classes:./heros/target/classes:/home/mvelezce/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.8.9/jackson-core-2.8.9.jar:/home/mvelezce/.m2/repository/com/fasterxml/jackson/core/jackson-annotations/2.8.9/jackson-annotations-2.8.9.jar:/home/mvelezce/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.8.9/jackson-databind-2.8.9.jar:/home/mvelezce/.m2/repository/org/slf4j/slf4j-api/1.7.5/slf4j-api-1.7.5.jar:/home/mvelezce/.m2/repository/org/slf4j/slf4j-simple/1.7.5/slf4j-simple-1.7.5.jar:/home/mvelezce/.m2/repository/com/google/guava/guava/18.0/guava-18.0.jar:/home/mvelezce/.m2/repository/commons-io/commons-io/2.5/commons-io-2.5.jar:/home/mvelezce/.m2/repository/org/ow2/asm/asm-debug-all/5.1/asm-debug-all-5.1.jar edu.cmu.cs.mvelezce.analysis.Run
     */

    protected static final String sep = System.getProperty("path.separator");
    protected static String appPath;
    protected static String libPath;
    protected static InfoflowConfiguration infoflowConfiguration;
    protected static IInfoflowConfig sootConfiguration;

    public static void main(String[] args) throws IOException {
        File f = new File(".");
        File testSrc1 = new File(f, "target" + File.separator + "classes");
        File testSrc2 = new File(f, "target" + File.separator + "test-classes");

        if(!(testSrc1.exists() || testSrc2.exists())) {
            throw new RuntimeException("Test aborted - none of the test sources are available");
        }

        appPath = testSrc1.getCanonicalPath() + sep + testSrc2.getCanonicalPath();//+ sep + testSrc3.getCanonicalPath();
        libPath = System.getProperty("java.home") + File.separator + "lib" + File.separator + "rt.jar"
                + sep + System.getProperty("java.home") + File.separator + "lib" + File.separator + "jce.jar";

// Config information flow
        infoflowConfiguration = new InfoflowConfiguration();
//        infoflowConfiguration.setCallgraphAlgorithm(InfoflowConfiguration.CallgraphAlgorithm.CHA);
        infoflowConfiguration.setCallgraphAlgorithm(InfoflowConfiguration.CallgraphAlgorithm.SPARK);
        infoflowConfiguration.setEnableImplicitFlows(true);
        infoflowConfiguration.setCodeEliminationMode(InfoflowConfiguration.CodeEliminationMode.NoCodeElimination);
//        infoflowConfiguration.setInspectSources(true);
        infoflowConfiguration.setInspectSinks(false);
        infoflowConfiguration.setAccessPathLength(10_000);
        infoflowConfiguration.setDataFlowSolver(InfoflowConfiguration.DataFlowSolver.ContextFlowSensitive);

//        infoflowConfiguration.setAliasingAlgorithm(InfoflowConfiguration.AliasingAlgorithm.None);
//        infoflowConfiguration.setFlowSensitiveAliasing(true);

        infoflowConfiguration.setAliasingAlgorithm(InfoflowConfiguration.AliasingAlgorithm.None);
        infoflowConfiguration.setFlowSensitiveAliasing(false);

//        infoflowConfiguration.setAliasingAlgorithm(InfoflowConfiguration.AliasingAlgorithm.FlowSensitive);
//        infoflowConfiguration.setFlowSensitiveAliasing(true);

//        infoflowConfiguration.setAliasingAlgorithm(InfoflowConfiguration.AliasingAlgorithm.FlowSensitive);
//        infoflowConfiguration.setFlowSensitiveAliasing(false);

//        infoflowConfiguration.setAliasingAlgorithm(InfoflowConfiguration.AliasingAlgorithm.PtsBased);
//        infoflowConfiguration.setFlowSensitiveAliasing(true);

//        infoflowConfiguration.setAliasingAlgorithm(InfoflowConfiguration.AliasingAlgorithm.PtsBased);
//        infoflowConfiguration.setFlowSensitiveAliasing(false);

        infoflowConfiguration.setStopAfterFirstFlow(false);
        infoflowConfiguration.setEnableStaticFieldTracking(true);
        infoflowConfiguration.setEnableExceptionTracking(false);
        infoflowConfiguration.setMaxThreadNum(1);
        infoflowConfiguration.setOneSourceAtATime(true);

//        infoflowConfiguration.setSequentialPathProcessing(true);

//        infoflowConfiguration.setWriteOutputFiles(true);
//        infoflowConfiguration.setUseThisChainReduction(true);
//        infoflowConfiguration.setUseRecursiveAccessPaths(true);
//        infoflowConfiguration.setExcludeSootLibraryClasses(false);
//        InfoflowConfiguration.setPathAgnosticResults(false);
//        infoflowConfiguration.setEnableExceptionTracking(true);
//        InfoflowConfiguration.setOneResultPerAccessPath(true);


        // Config soot
        sootConfiguration = new SootConfig();

        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/berkeley-db/out/production/berkeley-db");
        appPath = file + sep + appPath;

//        file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/berkeley-db/out/test/berkeley-db");
//        appPath = file + sep + appPath;

        String systemName = "berkeley-db";
        TaintInfoflow infoflow = new TaintInfoflow(systemName);

        // Configure analysis
        infoflow.setConfig(infoflowConfiguration);
        infoflow.setSootConfig(sootConfiguration);
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextSensitive, false));
        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitiveSourceFinder, false));
//        infoflow.setPathBuilderFactory(new DefaultPathBuilderFactory(DefaultPathBuilderFactory.PathBuilder.ContextInsensitive, true));

        // Add taint wrapper
//        EasyTaintWrapper easyWrapper = new EasyTaintWrapper(new File("src/main/java/edu/cmu/cs/mvelezce/analysis/EasyTaintWrapperSource.txt"));
//        infoflow.setTaintWrapper(easyWrapper);

        // Add entry points
        String entryPoint = "<persist.gettingStarted.ExampleInventoryRead: void main(java.lang.String[])>";

        List<String> entryPoints = new ArrayList<>();
        entryPoints.add(entryPoint);

        // Run
        infoflow.computeInfoflowOneSourceAtATime(appPath, libPath, entryPoint);
//        infoflow.computeInfoflowOneSourceAtATime(appPath, libPath, entryPoint, infoflow.getSources(), infoflow.getSinks());
//        infoflow.computeInfoflow(appPath, libPath, entryPoints, infoflow.getSources(), infoflow.getSinks());

        infoflow.aggregateInfoflowResults(0);
        infoflow.saveJimpleFiles();
        infoflow.saveDotStringFiles();
    }


}
