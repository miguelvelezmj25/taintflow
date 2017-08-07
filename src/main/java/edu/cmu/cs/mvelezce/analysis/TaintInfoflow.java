package edu.cmu.cs.mvelezce.analysis;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.analysis.option.Option;
import soot.jimple.infoflow.Infoflow;
import soot.jimple.infoflow.solver.cfg.IInfoflowCFG;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class TaintInfoflow extends Infoflow {
    private static final String CONFIG_FILE = "/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/src/main/java/edu/cmu/cs/mvelezce/analysis/option/config.json";

    private String systemName;
    private List<String> sources = new ArrayList<>();
    private List<String> sinks = new ArrayList<>();
    private Map<String, Option> optionsToObjects = new HashMap<>();

    public TaintInfoflow(String systemName) throws IOException {
        this.systemName = systemName;

        this.readSources();
        this.readSinks();
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
            String optionText = element.get("option").asText();
            Option option = new Option(optionText, methodSignature);

            this.optionsToObjects.put(optionText, option);
            this.sources.add(methodSignature);
        }
    }

    public IInfoflowCFG getiCfg() {
        return this.iCfg;
    }

    public List<String> getSources() {
        return sources;
    }

    public List<String> getSinks() {
        return sinks;
    }

    public Map<String, Option> getOptionsToObjects() {
        return optionsToObjects;
    }
}
