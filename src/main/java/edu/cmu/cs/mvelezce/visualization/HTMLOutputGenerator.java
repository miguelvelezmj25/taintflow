package edu.cmu.cs.mvelezce.visualization;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.analysis.TaintInfoflow;
import edu.cmu.cs.mvelezce.analysis.option.json.ControlFlowResult;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HTMLOutputGenerator extends HTMLBaseGenerator<ControlFlowResult> {

    public static final String OUTPUT_DIR = "output/";
    public static String HTML_HEAD_BEGIN = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <title>";

    public static String HTML_HEAD_END = "</title>\n" +
            "    <link rel=\"stylesheet\" type=\"text/css\" href=\"../../../css/style.css\">\n" +
            "    <link href=\"https://fonts.googleapis.com/css?family=Roboto+Mono:400,700\" rel=\"stylesheet\">\n" +
            "</head>\n";
    public static final String HTML_BODY = "<body>\n" +
            "\n" +
            "<div class=\"container\">\n" +
            "    <div class=\"container_1 grid_0 grid_1\">\n" +
            "        <div id=\"code_area\" class=\"container_0 grid_0 grid_1\">\n";
    public static final String HTML_END = "</div>\n" +
            "\n" +
            "    </div>\n" +
            "</div>\n" +
            "</body>\n" +
            "</html>";



    public HTMLOutputGenerator(String root, String systemName) {
        super(root, systemName);
    }

    @Override
    public void generateStaticHTMLPage(List<ControlFlowResult> results) throws IOException {
        String[] extensions = {"java"};

        Collection<File> files = FileUtils.listFiles(new File(this.getRoot()), extensions, true);

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

            this.setHtmlTitle(file.getName());

            StringBuilder staticHTMLPage = new StringBuilder();

            FileInputStream fstream = new FileInputStream(this.getRoot() + file.getPath().replace(this.getRoot(), ""));
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
                }
                else {
                    staticHTMLPage.append(strLine);
                }

                staticHTMLPage.append("</div>");
                staticHTMLPage.append("\n");
                lineNumber++;
            }

            in.close();

            File outputFile = new File(HTMLOutputGenerator.ROOT_DIR + this.getSystemName() + "/"
                    + HTMLOutputGenerator.OUTPUT_DIR + "/" + this.getSystemName() + "/" + file.getName() + ".html");
            outputFile.getParentFile().mkdirs();
            PrintWriter out = new PrintWriter(outputFile);
            out.print(HTMLOutputGenerator.HTML_HEAD_BEGIN + this.getHtmlTitle() + HTMLOutputGenerator.HTML_HEAD_END
                    + HTMLOutputGenerator.HTML_BODY + staticHTMLPage + HTMLOutputGenerator.HTML_END);
            out.flush();
            out.close();

            System.out.println("Generated " + file.getName());
        }
    }

    @Override
    public List<ControlFlowResult> readResults() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File inputFile = new File(TaintInfoflow.OUTPUT_DIR + "/" + this.getSystemName() + "/" + this.getSystemName() + ".json");
        List<ControlFlowResult> results = mapper.readValue(inputFile, new TypeReference<List<ControlFlowResult>>() {
        });

        return results;
    }
}
