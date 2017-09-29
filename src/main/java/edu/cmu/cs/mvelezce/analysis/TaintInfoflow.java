package edu.cmu.cs.mvelezce.analysis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.analysis.option.json.ControlFlowResult;
import edu.cmu.cs.mvelezce.analysis.option.json.SourceToSinkPath;
import edu.cmu.cs.mvelezce.soot.jimple.jtp.ControlFlowSink;
import org.apache.commons.io.FileUtils;
import soot.*;
import soot.jimple.InvokeExpr;
import soot.jimple.NopStmt;
import soot.jimple.Stmt;
import soot.jimple.infoflow.Infoflow;
import soot.jimple.infoflow.results.InfoflowResults;
import soot.jimple.infoflow.results.ResultSinkInfo;
import soot.jimple.infoflow.results.ResultSourceInfo;
import soot.jimple.internal.JInvokeStmt;
import soot.tagkit.BytecodeOffsetTag;
import soot.tagkit.LineNumberTag;
import soot.tagkit.Tag;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalGraph;
import soot.util.MultiMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class TaintInfoflow extends Infoflow {
    public static final String CONFIG_FILE = "src/main/java/edu/cmu/cs/mvelezce/analysis/option/config.json";
    public static final String OUTPUT_DIR = "src/main/resources/output/";
    public static final String PATHS_DIR = "src/main/resources/paths/";

    private String systemName;
    private Map<String, String> sourcesToOptions = new HashMap<>();
    private List<String> sinks = new ArrayList<>();
    private List<String> packages = new ArrayList<>();

    public TaintInfoflow(String systemName) throws IOException {
        this.systemName = systemName;

        this.readSources();
        this.readSinks();
        this.readPackages();
    }

    @Override
    protected void constructCallgraph() {
        super.constructCallgraph();

//        Iterator<MethodOrMethodContext> iter = Scene.v().getReachableMethods().listener();
//        PackManager.v().getPack("jtp").add(new Transform("jtp.controlflowsink", ControlFlowSink.v()));
////        PackManager.v().getPack("jtp").add(new Transform("jtp.trycatchlabelnop", TryCatchLabelNop.v()));
////        PackManager.v().getPack("jtp").add(new Transform("jtp.nop", Nop.v()));
//
//        while (iter.hasNext()) {
//            MethodOrMethodContext m = iter.next();
//            SootMethod method = m.method();
////
//            String methodPackage = method.getDeclaringClass().getPackageName();
//
//            for(String packageName : this.packages) {
////                if((!method.getDeclaringClass().isJavaLibraryClass() || !method.getDeclaringClass().isPhantomClass())
////                    /*&& methodPackage.contains(packageName)*/ && method.hasActiveBody()) {
//                if(methodPackage.contains(packageName) && method.hasActiveBody()) {
//                    PackManager.v().getPack("jtp").apply(method.getActiveBody());
//                }
//            }
//        }
    }

    public void aggregateInfoflowResults(int count) throws IOException {
        String[] extensions = {"json"};

        Collection<File> files = FileUtils.listFiles(new File(TaintInfoflow.OUTPUT_DIR + this.systemName), extensions, false);

        List<ControlFlowResult> aggregatedResults = new ArrayList<>();

        for(File file : files) {
            if(file.getName().contains(this.systemName + ".json")) {
                continue;
            }

            ObjectMapper mapper = new ObjectMapper();
            List<ControlFlowResult> results = mapper.readValue(file, new TypeReference<List<ControlFlowResult>>() {
            });

            for(ControlFlowResult controlFlowResult : results) {
                int index = aggregatedResults.indexOf(controlFlowResult);

                if(index < 0) {
                    aggregatedResults.add(controlFlowResult);
                }
                else {
                    ControlFlowResult currentResult = aggregatedResults.get(index);
                    Set<String> currentOptions = currentResult.getOptions();
                    currentOptions.addAll(controlFlowResult.getOptions());
                    currentResult.setOptions(currentOptions);
                    currentResult.setOptionCount(currentOptions.size());
                }
            }
        }


        ObjectMapper mapper = new ObjectMapper();
        File outputFile = new File(TaintInfoflow.OUTPUT_DIR + "/" + this.systemName + "/" + this.systemName + ".json");
        mapper.writeValue(outputFile, aggregatedResults);

        System.out.println("\n############## STATS");
        System.out.println("Number of results: " + aggregatedResults.size() + "\n");
        assert aggregatedResults.size() == count;
        this.calculateOptionToSinkCount(aggregatedResults);
        this.calculateInteractionOrder(aggregatedResults);
    }

    private void calculateInteractionOrder(List<ControlFlowResult> aggregatedResults) {
        Map<Integer, Integer> interactionCountsToEntryCount = new TreeMap<>();

        for(ControlFlowResult controlFlowResult : aggregatedResults) {
            int interactionOrder = controlFlowResult.getOptionCount();

            if(!interactionCountsToEntryCount.containsKey(interactionOrder)) {
                interactionCountsToEntryCount.put(interactionOrder, 0);
            }

            int currentEntryCount = interactionCountsToEntryCount.get(interactionOrder);
            interactionCountsToEntryCount.put(interactionOrder, currentEntryCount + 1);
        }

        for(Map.Entry<Integer, Integer> interactionCountToEntryCount : interactionCountsToEntryCount.entrySet()) {
            System.out.println("Interaction order= " + interactionCountToEntryCount.getKey() + " entries= " + interactionCountToEntryCount.getValue());
        }

        System.out.println("");
    }

    private void calculateOptionToSinkCount(List<ControlFlowResult> aggregatedResults) {
        Map<String, Integer> optionsToSinkCount = new HashMap<>();

        for(String option : this.sourcesToOptions.values()) {
            int count = 0;

            for(ControlFlowResult controlFlowResult : aggregatedResults) {
                if(controlFlowResult.getOptions().contains(option)) {
                    count++;
                }
            }

            optionsToSinkCount.put(option, count);
        }

        for(Map.Entry<String, Integer> optionToSinkCount : optionsToSinkCount.entrySet()) {
            System.out.println(optionToSinkCount.getKey() + " -> " + optionToSinkCount.getValue());
        }

        System.out.println("");
    }

    public void computeInfoflowOneSourceAtATime(String libPath, String appPath, String entryPoint) throws IOException {
        this.computeInfoflowOneSourceAtATime(libPath, appPath, entryPoint, this.sourcesToOptions.keySet(), this.sinks);
    }

    public void computeInfoflowOneSourceAtATime(String libPath, String appPath, String entryPoint, Collection<String> sources,
                                                Collection<String> sinks) throws IOException {

        File outputFile = new File(TaintInfoflow.OUTPUT_DIR + "/" + this.systemName + "/");

        if(outputFile.exists()) {
            FileUtils.forceDelete(outputFile);
        }

        for(String source : sources) {
            String currentOption = this.sourcesToOptions.get(source);

            System.out.println("############## Analyzing option " + currentOption);

            List<String> entryPoints = new ArrayList<>();
            entryPoints.add(entryPoint);

            List<String> intermediateSources = new ArrayList<>();
            intermediateSources.add(source);

            this.computeInfoflow(libPath, appPath, entryPoints, intermediateSources, sinks);

//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            // TODO check for exceptions thrown while the analysis executed
            this.saveResults(currentOption);

            if(this.pathBuilderFactory.supportsPathReconstruction()) {
                this.getSourceToSinkPath(currentOption);
            }

            System.out.println("############## Option " + currentOption + " results size " + this.getResults().size());

//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

        }
    }

    private void getSourceToSinkPath(String option) throws IOException {
        List<SourceToSinkPath> paths = new ArrayList<>();
        MultiMap<ResultSinkInfo, ResultSourceInfo> resultMap = this.getResults().getResults();

        for(ResultSinkInfo sink : resultMap.keySet()) {

            if(resultMap.get(sink).size() != 1) {
//                throw new RuntimeException("Something weird with paths");
            }

            for(ResultSourceInfo source : resultMap.get(sink)) {
                List<SourceToSinkPath.PathElement> path = new ArrayList<>();

                if(source.getPath() != null) {
                    for(Unit p : source.getPath()) {
                        SootMethod method = this.iCfg.getMethodOf(p);
                        String fullName = method.getDeclaringClass().getName();

                        List<Integer> javaLines = new ArrayList<>();

                        for(Tag tag : p.getTags()) {
                            if(tag instanceof LineNumberTag) {
                                int javaLine = ((LineNumberTag) tag).getLineNumber();
                                javaLines.add(javaLine);
                            }
                        }

                        if(javaLines.isEmpty()) {
                            javaLines.add(-1);
                        }

                        int javaLine;

                        if(javaLines.size() == 1) {
                            javaLine = javaLines.get(0);
                        }
                        else {
                            int index = javaLines.indexOf(Collections.min(javaLines));
                            javaLine = javaLines.get(index);
                        }

                        SourceToSinkPath.PathElement element = new SourceToSinkPath.PathElement(fullName, javaLine);
                        path.add(element);
                    }
                }

                SourceToSinkPath sourceToSinkPath = new SourceToSinkPath(path);
                paths.add(sourceToSinkPath);
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        File outputFile = new File(TaintInfoflow.PATHS_DIR + "/" + this.systemName + "/" + option + ".json");
        outputFile.getParentFile().mkdirs();
        mapper.writeValue(outputFile, paths);
    }

    private void saveResults(String option) throws IOException {
        MultiMap<ResultSinkInfo, ResultSourceInfo> resultMap = this.getResults().getResults();
        List<ControlFlowResult> controlFlowResults = new ArrayList<>();

        for(ResultSinkInfo resultSinkInfo : resultMap.keySet()) {
            Stmt sink = resultSinkInfo.getSink();

            // Sink
            SootMethod sinkAtMethod = this.iCfg.getMethodOf(sink);
            SootClass sinkAtClass = sinkAtMethod.getDeclaringClass();

            String packageName = sinkAtClass.getPackageName();
            String className = sinkAtClass.getShortName();
            String methodSignature = sinkAtMethod.getBytecodeSignature();
            methodSignature = methodSignature.substring(methodSignature.indexOf(":") + 1, methodSignature.length() - 1);
            methodSignature = methodSignature.trim();

            List<Integer> bytecodeIndexes = new ArrayList<>();

            for(Tag tag : sink.getTags()) {
                if(tag instanceof BytecodeOffsetTag) {
                    int bytecodeIndex = ((BytecodeOffsetTag) tag).getBytecodeOffset();
                    bytecodeIndexes.add(bytecodeIndex);
                }
            }

            if(bytecodeIndexes.isEmpty()) {
                bytecodeIndexes.add(-1);
            }

            int bytecodeIndex;

            if(bytecodeIndexes.size() == 1) {
                bytecodeIndex = bytecodeIndexes.get(0);
            }
            else {
                int index = bytecodeIndexes.indexOf(Collections.min(bytecodeIndexes));
                bytecodeIndex = bytecodeIndexes.get(index);
            }

            List<Integer> javaLines = new ArrayList<>();

            for(Tag tag : sink.getTags()) {
                if(tag instanceof LineNumberTag) {
                    int javaLine = ((LineNumberTag) tag).getLineNumber();
                    javaLines.add(javaLine);
                }
            }

            if(javaLines.isEmpty()) {
                javaLines.add(-1);
            }

            int javaLine;

            if(javaLines.size() == 1) {
                javaLine = javaLines.get(0);
            }
            else {
                int index = javaLines.indexOf(Collections.min(javaLines));
                javaLine = javaLines.get(index);
            }

            // Source
            Set<String> sources = new HashSet<>();

            for(ResultSourceInfo resultSourceInfo : resultMap.values()) {
                Stmt source = resultSourceInfo.getSource();

                if(!source.containsInvokeExpr()) {
                    throw new RuntimeException("The source does not have an invoke: " + source.toString());
                }

                InvokeExpr expr = source.getInvokeExpr();
                SootMethod exprMethod = expr.getMethod();
                String resultOption = this.sourcesToOptions.get(exprMethod.getSignature());

                if(resultOption == null) {
                    throw new RuntimeException("Source is not associated to an option: " + source);
                }

                if(!resultOption.equals(option)) {
                    throw new RuntimeException("The option in this source is different than the one we analyzed");
                }

                sources.add(option);
            }

            // Saving
            ControlFlowResult controlFlowResult = new ControlFlowResult(packageName, className, methodSignature,
                    bytecodeIndex, javaLine, sources);

            controlFlowResults.add(controlFlowResult);
        }

        ObjectMapper mapper = new ObjectMapper();
        File outputFile = new File(TaintInfoflow.OUTPUT_DIR + "/" + this.systemName + "/" + option + ".json");
        outputFile.getParentFile().mkdirs();
        mapper.writeValue(outputFile, controlFlowResults);
    }

    // TODO delete
    public void checkResults() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(!this.isResultAvailable()) {
            return;
        }

        InfoflowResults map = this.getResults();

        for(ResultSinkInfo resultSinkInfo : map.getResults().keySet()) {
            Set<ResultSourceInfo> resultSources = map.getResults().get(resultSinkInfo);
//            this.printSinkResult(resultSinkInfo.getSink(), resultSources);
            System.out.println("");
        }

        this.saveResults(map);
    }

    private void saveResults(InfoflowResults map) {
        List<ControlFlowResult> controlFlowResults = new ArrayList<>();

        for(ResultSinkInfo resultSinkInfo : map.getResults().keySet()) {
            Stmt sink = resultSinkInfo.getSink();

            // Sink
            SootMethod sinkAtMethod = this.iCfg.getMethodOf(sink);
            SootClass sinkAtClass = sinkAtMethod.getDeclaringClass();

            String packageName = sinkAtClass.getPackageName();
            String className = sinkAtClass.getShortName();
            String methodSignature = sinkAtMethod.getBytecodeSignature();

            List<Integer> bytecodeIndexes = new ArrayList<>();

            for(Tag tag : sink.getTags()) {
                if(tag instanceof BytecodeOffsetTag) {
                    int bytecodeIndex = ((BytecodeOffsetTag) tag).getBytecodeOffset();
                    bytecodeIndexes.add(bytecodeIndex);
                }
            }

            if(bytecodeIndexes.isEmpty()) {
                bytecodeIndexes.add(-1);
            }

            int bytecodeIndex;

            if(bytecodeIndexes.size() == 1) {
                bytecodeIndex = bytecodeIndexes.get(0);
            }
            else {
                bytecodeIndex = bytecodeIndexes.indexOf(Collections.min(bytecodeIndexes));
            }

            List<Integer> javaLines = new ArrayList<>();

            for(Tag tag : sink.getTags()) {
                if(tag instanceof LineNumberTag) {
                    int javaLine = ((LineNumberTag) tag).getLineNumber();
                    javaLines.add(javaLine);
                }
            }

            if(javaLines.isEmpty()) {
                javaLines.add(-1);
            }

            int javaLine;

            if(javaLines.size() == 1) {
                javaLine = javaLines.get(0);
            }
            else {
                javaLine = javaLines.indexOf(Collections.min(javaLines));
            }

            // Source
            Set<String> sources = new HashSet<>();

            for(ResultSourceInfo resultSourceInfo : map.getResults().get(resultSinkInfo)) {
                Stmt source = resultSourceInfo.getSource();

                if(!source.containsInvokeExpr()) {
                    throw new RuntimeException("The source does not have an invoke: " + source.toString());
                }

                InvokeExpr expr = source.getInvokeExpr();
                SootMethod exprMethod = expr.getMethod();
                String option = this.sourcesToOptions.get(exprMethod.getSignature());

                if(option == null) {
                    throw new RuntimeException("Source is not associated to an option: " + source);
                }

                sources.add(option);
            }

            // Saving
            ControlFlowResult controlFlowResult = new ControlFlowResult(packageName, className, methodSignature,
                    bytecodeIndex, javaLine, sources);

            controlFlowResults.add(controlFlowResult);
        }

        Map<Integer, Integer> interactionCountsToEntryCount = new TreeMap<>();

        for(ResultSinkInfo resultSinkInfo : map.getResults().keySet()) {
            int sourceCount = map.getResults().get(resultSinkInfo).size();

            if(!interactionCountsToEntryCount.containsKey(sourceCount)) {
                interactionCountsToEntryCount.put(sourceCount, 0);
            }

            int currentEntryCount = interactionCountsToEntryCount.get(sourceCount);
            interactionCountsToEntryCount.put(sourceCount, currentEntryCount + 1);
        }

        for(Map.Entry<Integer, Integer> interactionCountToEntryCount : interactionCountsToEntryCount.entrySet()) {
            System.out.println("Interaction order= " + interactionCountToEntryCount.getKey() + " entries= " + interactionCountToEntryCount.getValue());
        }

        ObjectMapper mapper = new ObjectMapper();
        File outputFile = new File("src/main/resources/" + this.systemName + ".json");

        try {
            mapper.writeValue(outputFile, controlFlowResults);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void printSinkResult(Stmt sink, Set<ResultSourceInfo> sources) {
        System.out.println("Sink: " + sink);

        SootMethod sinkAtMethod = this.iCfg.getMethodOf(sink);
        SootClass sinkAtClass = sinkAtMethod.getDeclaringClass();
        String sinkAtPackage = sinkAtClass.getPackageName();
        System.out.println("sink at method: " + sinkAtMethod.getName());
        System.out.println("sink at method bytecode signature: " + sinkAtMethod.getBytecodeSignature());
        System.out.println("sink at method signature: " + sinkAtMethod.getSignature());
        System.out.println("sink at method sub signature: " + sinkAtMethod.getSubSignature());
        System.out.println("sink at class: " + sinkAtClass.getShortName());
        System.out.println("sink at package: " + sinkAtPackage);

        List<Integer> bytecodeIndexes = new ArrayList<>();

        for(Tag tag : sink.getTags()) {
            if(tag instanceof BytecodeOffsetTag) {
                int bytecodeIndex = ((BytecodeOffsetTag) tag).getBytecodeOffset();
                bytecodeIndexes.add(bytecodeIndex);
            }
        }

        if(bytecodeIndexes.isEmpty()) {
            bytecodeIndexes.add(-1);
        }

        System.out.println("Bytecode indexes: " + bytecodeIndexes);

        for(ResultSourceInfo resultSourceInfo : sources) {
            Stmt source = resultSourceInfo.getSource();
            System.out.println("Source: " + source);
        }
    }

    private void readSinks() throws IOException {
        File file = new File(TaintInfoflow.CONFIG_FILE);
        ObjectMapper mapper = new ObjectMapper();

        JsonNode node = mapper.readTree(file);
        JsonNode systemNode = node.get(systemName);
        JsonNode sources = systemNode.get("sinks");

        Iterator<JsonNode> it = sources.elements();

        while (it.hasNext()) {
            JsonNode element = it.next();
            String methodSignature = element.get("method").asText();

            this.sinks.add(methodSignature);
        }
    }

    private void readPackages() throws IOException {
        File file = new File(TaintInfoflow.CONFIG_FILE);
        ObjectMapper mapper = new ObjectMapper();

        JsonNode node = mapper.readTree(file);
        JsonNode systemNode = node.get(systemName);
        JsonNode sources = systemNode.get("packages");

        Iterator<JsonNode> it = sources.elements();

        while (it.hasNext()) {
            JsonNode element = it.next();
            String methodSignature = element.get("package").asText();

            this.packages.add(methodSignature);
        }
    }

    private void readSources() throws IOException {
        File file = new File(TaintInfoflow.CONFIG_FILE);
        ObjectMapper mapper = new ObjectMapper();

        JsonNode node = mapper.readTree(file);
        JsonNode systemNode = node.get(systemName);
        JsonNode sources = systemNode.get("sources");

        Iterator<JsonNode> it = sources.elements();

        while (it.hasNext()) {
            JsonNode element = it.next();
            String methodSignature = element.get("method").asText();
            String option = element.get("option").asText();
//            Option option = new Option(optionText, methodSignature);

            this.sourcesToOptions.put(methodSignature, option);
//            this.sources.add(methodSignature);
        }
    }

    public List<String> getSources() {
        return new ArrayList<>(this.sourcesToOptions.keySet());
    }

    public List<String> getSinks() {
        return sinks;
    }

    public Map<String, String> getSourcesToOptions() {
        return this.sourcesToOptions;
    }

    public void saveJimpleFiles() throws FileNotFoundException {
        List<SootMethod> methods = new ArrayList<>(this.getMethodsForSeeds(this.iCfg));

        Map<String, StringBuilder> classesToMethods = new HashMap<>();

        for(SootMethod method : methods) {
            if(!method.hasActiveBody()) {
                continue;
            }

//            if(method.getName().contains("main")) {
//                System.out.println(this.iCfg.getCallersOf(method));
//            }

            String className = method.getDeclaringClass().getName();

            if(!classesToMethods.containsKey(className)) {
                classesToMethods.put(className, new StringBuilder());
            }

            StringBuilder classBody = classesToMethods.get(className);
            classBody.append(method.getActiveBody().toString());
            classBody.append("\n");
        }

        String root = "sootOutput/";

        for(Map.Entry<String, StringBuilder> classToMethods : classesToMethods.entrySet()) {
            PrintWriter out = new PrintWriter(root + classToMethods.getKey());
            out.println(classToMethods.getValue());
            out.close();
            out.flush();
        }
    }

    public void saveDotStringFiles() throws FileNotFoundException {
        List<SootMethod> methods = new ArrayList<>(this.getMethodsForSeeds(this.iCfg));

        Map<String, StringBuilder> classesToDoStrings = new HashMap<>();

        for(SootMethod method : methods) {
            if(!method.hasActiveBody()) {
                continue;
            }

            String className = method.getDeclaringClass().getName();

            if(!classesToDoStrings.containsKey(className)) {
                classesToDoStrings.put(className, new StringBuilder());
            }

            StringBuilder classDotString = classesToDoStrings.get(className);
            classDotString.append("digraph ");
            classDotString.append(method.getName());
            classDotString.append(" {\n");

            DirectedGraph<Unit> graph = this.iCfg.getOrCreateUnitGraph(method);

//            if(className.contains("Interaction")) {
//                System.out.println();
//            }

            Iterator<Unit> units = graph.iterator();
            Set<String> instrumentedNodes = new HashSet<>();

            while (units.hasNext()) {
                Unit unit = units.next();
                List<Unit> succs = graph.getSuccsOf(unit);

                for(Unit succ : succs) {
                    String node = unit.toString().replace("\"", "\\\"");
                    node += " {" + unit.hashCode() + "}";

                    if(unit instanceof NopStmt) {
                        instrumentedNodes.add(node);
                    }
                    else if(unit instanceof JInvokeStmt) {
                        InvokeExpr v = ((JInvokeStmt) unit).getInvokeExpr();
                        String name = v.getMethod().getName();

                        if(name.equals("sink")) {
                            instrumentedNodes.add(node);
                        }
                    }

                    classDotString.append('"');
                    classDotString.append(node);
                    classDotString.append('"');
                    classDotString.append(" -> ");

                    node = succ.toString().replace("\"", "\\\"");
                    node += " {" + succ.hashCode() + "}";

                    if(succ instanceof NopStmt) {
                        instrumentedNodes.add(node);
                    }
                    else if(succ instanceof JInvokeStmt) {
                        InvokeExpr v = ((JInvokeStmt) succ).getInvokeExpr();
                        String name = v.getMethod().getName();

                        if(name.equals("sink")) {
                            instrumentedNodes.add(node);
                        }
                    }

                    classDotString.append('"');
                    classDotString.append(node);
                    classDotString.append('"');

                    if(graph instanceof ExceptionalGraph) {
                        ExceptionalGraph<Unit> exceptionalGraph = (ExceptionalGraph<Unit>) graph;
                        if(exceptionalGraph.getExceptionalSuccsOf(unit).contains(succ)) {
                            classDotString.append("[penwidth=3, color=\"red\"]");
                        }
                    }

                    classDotString.append(";\n");
                }
            }

            for(String nopNode : instrumentedNodes) {
                classDotString.append('"');
                classDotString.append(nopNode);
                classDotString.append('"');
                classDotString.append("[fontcolor=\"blue\", penwidth=3, color=\"blue\"];\n");
            }

            classDotString.append("}\n\n");

        }

        String root = "dotStringOutput/";

        for(Map.Entry<String, StringBuilder> classDotString : classesToDoStrings.entrySet()) {
            PrintWriter out = new PrintWriter(root + classDotString.getKey());
            out.println(classDotString.getValue());
            out.close();
            out.flush();
        }
    }

}
