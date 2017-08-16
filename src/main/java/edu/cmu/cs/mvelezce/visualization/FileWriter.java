package edu.cmu.cs.mvelezce.visualization;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.analysis.option.json.ControlFlowResult;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileWriter {

    public void readFile(String systemName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File inputFile = new File("src/main/resources/" + systemName + ".json");
        List<ControlFlowResult> results = mapper.readValue(inputFile, List.class);
        System.out.println(results.size());
    }
}
