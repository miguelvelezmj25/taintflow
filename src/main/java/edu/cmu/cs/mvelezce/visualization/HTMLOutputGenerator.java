package edu.cmu.cs.mvelezce.visualization;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.analysis.option.json.ControlFlowResult;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.*;

public class HTMLOutputGenerator {

    public static final String OUTPUT_DIR = "/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/src/visualization/";
    public static final String HTML_BEGIN = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <title>Example.java</title>\n" +
            "    <link rel=\"stylesheet\" type=\"text/css\" href=\"../css/style.css\">\n" +
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

    public String HTML_TITLE = "Default";

    private String root;
    private String systemName;

    public HTMLOutputGenerator(String root, String systemName) {
        this.root = root;
        this.systemName = systemName;
    }

    public void generateHTMLPage() throws IOException {
        List<ControlFlowResult> results = this.readResults();
        this.generateStaticHTMLPage(results);
    }

    public void generateStaticHTMLPage(List<ControlFlowResult> results) throws IOException {
        String[] extensions = {"java"};

        Collection<File> files = FileUtils.listFiles(new File(this.root), extensions, true);

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

            this.HTML_TITLE = file.getName();

            StringBuilder staticHTMLPage = new StringBuilder();

            FileInputStream fstream = new FileInputStream(this.root + file.getPath().replace(this.root, ""));
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            int lineNumber = 1;

            while ((strLine = br.readLine()) != null) {
                if(linesToConstraints.containsKey(lineNumber)) {
                    staticHTMLPage.append("<div style=\"background-color:lawngreen;\">");
                }
                else {
                    staticHTMLPage.append("<div>");
                }

                staticHTMLPage.append(lineNumber);
                staticHTMLPage.append("&emsp;&emsp;");

                for(int i = 0; !strLine.isEmpty() && strLine.charAt(i) == ' '; i++) {
                    staticHTMLPage.append("&nbsp;");
                }

                if(linesToConstraints.containsKey(lineNumber)) {
                    staticHTMLPage.append("<b>");
                    staticHTMLPage.append(strLine);
                    staticHTMLPage.append("&nbsp;");
                    staticHTMLPage.append("&#8594; ").append(linesToConstraints.get(lineNumber));
                    staticHTMLPage.append("</b>");

                    staticHTMLPage.append("&emsp;");
                }
                else {
                    staticHTMLPage.append(strLine);
                }

                staticHTMLPage.append("</div>");
                staticHTMLPage.append("\n");
                lineNumber++;
            }

            in.close();

            File outputFile = new File(HTMLOutputGenerator.OUTPUT_DIR + this.systemName + "/" + file.getName() + ".html");
            outputFile.getParentFile().mkdirs();
            PrintWriter out = new PrintWriter(outputFile);
            out.print(HTMLOutputGenerator.HTML_BEGIN + staticHTMLPage + HTMLOutputGenerator.HTML_END);
            out.flush();
            out.close();

            System.out.println("Generated " + file.getName());
        }
    }

    public List<ControlFlowResult> readResults() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File inputFile = new File("src/main/resources/" + this.systemName + ".json");
        List<ControlFlowResult> results = mapper.readValue(inputFile, new TypeReference<List<ControlFlowResult>>() {
        });

        return results;
    }
}
