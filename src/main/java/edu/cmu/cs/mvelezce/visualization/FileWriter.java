package edu.cmu.cs.mvelezce.visualization;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.analysis.option.json.ControlFlowResult;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileWriter {

    private static final String ANNOTATED_EXTENSION = "_ANNOTATED.txt";

    public void writeAnnotatedFile(String system, String root) throws IOException {
        List<ControlFlowResult> results = this.readResults(system);
        this.writeAnnotatedFile(root, results);
    }

    public static void deleteAnnotatedFiles(String root) throws IOException {
        Collection<File> files = FileUtils.listFiles(new File(root), null, true);

        for(File file : files) {
            if(file.getName().endsWith(FileWriter.ANNOTATED_EXTENSION)) {
                FileUtils.forceDelete(file);
            }
        }
    }

    public void writeAnnotatedFile(String root, List<ControlFlowResult> results) throws IOException {
        String[] extensions = {"java"};

        Collection<File> files = FileUtils.listFiles(new File(root), extensions, true);

        for(File file : files) {
            Map<Integer, String> linesToConstraints = new HashMap<>();

            for(ControlFlowResult result : results) {
                String resultPackage = result.getPackageName();
                String resultClassName = result.getClassName();

                String resultFilePath = resultPackage.replace(".", "/") + "/" + resultClassName + ".java";

                if(file.getPath().contains(resultFilePath)) {
                    linesToConstraints.put(result.getJavaLine(), result.getOptions().toString());
                }

            }

            if(linesToConstraints.isEmpty()) {
                continue;
            }

            StringBuilder annotatedFile = new StringBuilder();

            FileInputStream fstream = new FileInputStream(root + file.getPath().replace(root, ""));
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            int lineNumber = 1;

            while ((strLine = br.readLine()) != null) {
//                annotatedFile.append(lineNumber).append("    ");
                annotatedFile.append(strLine).append("  ");

                if(linesToConstraints.containsKey(lineNumber)) {
                    annotatedFile.append("// -> ").append(linesToConstraints.get(lineNumber));
                }

                annotatedFile.append("\n");
                lineNumber++;
            }

            in.close();

            try (PrintWriter out = new PrintWriter(file.getAbsolutePath().replace(".java", "") + FileWriter.ANNOTATED_EXTENSION)) {
                out.print(annotatedFile.toString());

                System.out.println("Annotated " + file.getName());
            }
        }
    }

    public List<ControlFlowResult> readResults(String systemName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File inputFile = new File("src/main/resources/" + systemName + ".json");
        List<ControlFlowResult> results = mapper.readValue(inputFile, new TypeReference<List<ControlFlowResult>>() {
        });

        return results;
    }
}
