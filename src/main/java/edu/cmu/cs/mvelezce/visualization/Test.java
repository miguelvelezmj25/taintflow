package edu.cmu.cs.mvelezce.visualization;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.analysis.option.json.ControlFlowResult;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.*;

public class Test {

    public static final String HTML_BEGIN = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <title>Example.java</title>\n" +
            "    <link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\">\n" +
            "    <link href=\"https://fonts.googleapis.com/css?family=Roboto+Mono:400,700\" rel=\"stylesheet\">\n" +
            "</head>\n" +
            "<body>\n" +
            "\n" +
            "<div class=\"container\">\n" +
            "    <div class=\"container_1 grid_0 grid_1\">\n" +
            "        <div id=\"code_area\" class=\"container_0 grid_0 grid_1\">\n";
    public static final String HTML_END = "</div>\n" +
            "\n" +
            "    </div>\n" +
            "</body>\n" +
            "</html>";

    public static String HTML_TITLE = "Default";

    public static void main(String[] args) throws IOException {
        List<ControlFlowResult> results = Test.readResults("running-example");
        String root = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/running-example/src/main/java";
//        Set<String> uniqueClasses = Test.getUniqueClasses(results);
//        Set<File> uniqueFiles = Test.getUniqueFiles(root, uniqueClasses);
//        Test.generateHTML(uniqueClasses);
        Test.writeAnnotatedFile(root, results);
    }

    private static Set<File> getUniqueFiles(String root, Set<String> uniqueClasses) {
        Set<File> files = new HashSet<>();

        for(String className : uniqueClasses) {
            String classPath = className.replace(".", "/");
            classPath += ".java";
            File file = new File(root, classPath);
            files.add(file);
        }

        return files;
    }

    private static Set<String> getUniqueClasses(List<ControlFlowResult> results) {
        Set<String> result = new HashSet<>();

        for(ControlFlowResult controlFlowResult : results) {
            result.add(controlFlowResult.getPackageName() + "." + controlFlowResult.getClassName());
        }

        return result;
    }

    private static void generateHTML(Set<String> results) {
        StringBuilder s = new StringBuilder();

        for(String result : results) {
            s.append("<div>");
            s.append(result);
            s.append("</div>");
            s.append("\n");
        }


        System.out.println(HTML_BEGIN + s + HTML_END);
    }

//    public void writeAnnotatedFile(String system, String root) throws IOException {
//        List<ControlFlowResult> results = this.readResults(system);
//        this.writeAnnotatedFile(root, results);
//    }

    public static void writeAnnotatedFile(String root, List<ControlFlowResult> results) throws IOException {
        String[] extensions = {"java"};

        Collection<File> files = FileUtils.listFiles(new File(root), extensions, true);

        for(File file : files) {
            Map<Integer, String> linesToConstraints = new HashMap<>();

            for(ControlFlowResult result : results) {
                String resultPackage = result.getPackageName();
                String resultClassName = result.getClassName();

                String resultFilePath = resultPackage.replace(".", "/") + "/" + resultClassName + ".java";

                if(file.getPath().contains(resultFilePath)) {
                    if(linesToConstraints.containsKey(result.getJavaLine())) {
                        String currentOptions = linesToConstraints.get(result.getJavaLine());
                        currentOptions += " & " + result.getOptions();
                        linesToConstraints.put(result.getJavaLine(), currentOptions);
                    }
                    else {
                        linesToConstraints.put(result.getJavaLine(), result.getOptions().toString());
                    }
                }

            }

            if(linesToConstraints.isEmpty()) {
                continue;
            }

            HTML_TITLE = file.getName();

            StringBuilder annotatedFile = new StringBuilder();

            FileInputStream fstream = new FileInputStream(root + file.getPath().replace(root, ""));
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            int lineNumber = 1;

            while ((strLine = br.readLine()) != null) {
                if(linesToConstraints.containsKey(lineNumber)) {
                    annotatedFile.append("<div style=\"background-color:lawngreen;\">");
                }
                else {
                    annotatedFile.append("<div>");
                }

                annotatedFile.append(lineNumber);
                annotatedFile.append("&emsp;&emsp;");

                for(int i = 0; !strLine.isEmpty() && strLine.charAt(i) == ' '; i++) {
                    annotatedFile.append("&nbsp;");
                }

                if(linesToConstraints.containsKey(lineNumber)) {
                    annotatedFile.append("<b>");
                    annotatedFile.append(strLine);
                    annotatedFile.append("&nbsp;");
                    annotatedFile.append("&#8594; ").append(linesToConstraints.get(lineNumber));
                    annotatedFile.append("</b>");

                    annotatedFile.append("&emsp;");
                }
                else {
                    annotatedFile.append(strLine);
                }

                annotatedFile.append("</div>");
                annotatedFile.append("\n");
                lineNumber++;
            }

            in.close();

            File outputFile = new File("/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/src/visualization/Test.html");
            PrintWriter out = new PrintWriter(outputFile);
            out.print(HTML_BEGIN + annotatedFile + HTML_END);
            out.flush();
            out.close();

            System.out.println("Annotated " + file.getName());
        }
    }

    public static List<ControlFlowResult> readResults(String systemName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File inputFile = new File("src/main/resources/" + systemName + ".json");
        List<ControlFlowResult> results = mapper.readValue(inputFile, new TypeReference<List<ControlFlowResult>>() {
        });

        return results;
    }
}
