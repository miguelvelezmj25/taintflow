package edu.cmu.cs.mvelezce.analysis;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.analysis.option.json.ControlFlowResult;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.infoflow.Infoflow;
import soot.jimple.infoflow.results.InfoflowResults;
import soot.jimple.infoflow.results.ResultSinkInfo;
import soot.jimple.infoflow.results.ResultSourceInfo;
import soot.jimple.infoflow.solver.cfg.IInfoflowCFG;
import soot.tagkit.BytecodeOffsetTag;
import soot.tagkit.LineNumberTag;
import soot.tagkit.Tag;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class TaintInfoflow extends Infoflow {
    private static final String CONFIG_FILE = "/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/src/main/java/edu/cmu/cs/mvelezce/analysis/option/config.json";

    private String systemName;
    //    private List<String> sources = new ArrayList<>();
    private List<String> sinks = new ArrayList<>();
    private Map<String, String> sourcesToOptions = new HashMap<>();

    public TaintInfoflow(String systemName) throws IOException {
        this.systemName = systemName;

        this.readSources();
        this.readSinks();
    }

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
            SootMethod sinkAtMethod = this.getiCfg().getMethodOf(sink);
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
        File outputFile = new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/src/main/resources/" + this.systemName + ".json");

        try {
            mapper.writeValue(outputFile, controlFlowResults);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

//    private void saveResults(Stmt sink, Set<ResultSourceInfo> sourcesInfo) {
//        // Sink
//        SootMethod sinkAtMethod = this.getiCfg().getMethodOf(sink);
//        SootClass sinkAtClass = sinkAtMethod.getDeclaringClass();
//
//        String packageName = sinkAtClass.getPackageName();
//        String className = sinkAtClass.getShortName();
//        String methodSignature = sinkAtMethod.getBytecodeSignature();
//
//        List<Integer> bytecodeIndexes = new ArrayList<>();
//
//        for(Tag tag : sink.getTags()) {
//            if(tag instanceof BytecodeOffsetTag) {
//                int bytecodeIndex = ((BytecodeOffsetTag) tag).getBytecodeOffset();
//                bytecodeIndexes.add(bytecodeIndex);
//            }
//        }
//
//        if(bytecodeIndexes.isEmpty()) {
//            bytecodeIndexes.add(-1);
//        }
//
//        int bytecodeIndex = -1;
//
//        if(bytecodeIndexes.size() == 1) {
//            bytecodeIndex = bytecodeIndexes.get(0);
//        }
//        else {
//            bytecodeIndex = bytecodeIndexes.indexOf(Collections.min(bytecodeIndexes));
//        }
//
//
//        // Source
//        Set<String> sources = new HashSet<>();
//
//        for(ResultSourceInfo resultSourceInfo : sourcesInfo) {
//            Stmt source = resultSourceInfo.getSource();
//
//            if(!source.containsInvokeExpr()) {
//                throw new RuntimeException("The source does not have an invoke: " + source.toString());
//            }
//
//            InvokeExpr expr = source.getInvokeExpr();
//            SootMethod exprMethod = expr.getMethod();
//            String option = this.sourcesToOptions.get(exprMethod.getSignature());
//
//            if(option == null) {
//                throw new RuntimeException("Source is not associated to an option: " + source);
//            }
//
//            sources.add(option);
//        }
//
//        // Saving
//        ControlFlowResult controlFlowResult = new ControlFlowResult(packageName, className, methodSignature,
//                bytecodeIndex, sources);
//
//        ObjectMapper mapper = new ObjectMapper();
//        File outputFile = new File(this.systemName + ".json");
//
//        try {
//            mapper.writeValue(outputFile, controlFlowResult);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

    private void printSinkResult(Stmt sink, Set<ResultSourceInfo> sources) {
        System.out.println("Sink: " + sink);

        SootMethod sinkAtMethod = this.getiCfg().getMethodOf(sink);
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

    public IInfoflowCFG getiCfg() {
        return this.iCfg;
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

        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/sootOutput/";

        for(Map.Entry<String, StringBuilder> classToMethods : classesToMethods.entrySet()) {
            PrintWriter out = new PrintWriter(root + classToMethods.getKey());
            out.println(classToMethods.getValue());
            out.close();
            out.flush();
        }
    }
}
