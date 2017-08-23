package edu.cmu.cs.mvelezce.visualization;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.analysis.TaintInfoflow;
import edu.cmu.cs.mvelezce.analysis.option.json.SourceToSinkPath;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.*;

public class HTMLPathGenerator extends HTMLBaseGenerator<SourceToSinkPath> {

    public static final String PATH_DIR = "path/";
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
            "    <div id=\"code_area\" class=\"container_0 grid_0 grid_1\">\n";
    public static final String HTML_END = "</div>\n" +
            "    </div>\n" +
            "</body>\n" +
            "</html>";

    private String option;

    public HTMLPathGenerator(String root, String systemName, String option) {
        super(root, systemName);
        this.option = option;
    }

    public static void generateHTMLForSystem(String root, String systemName) throws IOException {
        String[] extensions = {"json"};

        Collection<File> files = FileUtils.listFiles(new File(TaintInfoflow.PATHS_DIR + systemName), extensions, true);
        Set<File> filesToAnalyze = new HashSet<>();

        for(File file : files) {
            if(file.getPath().contains(TaintInfoflow.PATHS_DIR + systemName + "/")) {
                filesToAnalyze.add(file);
            }
        }

        for(File file : filesToAnalyze) {
           String option = file.getName().replace(".json", "");
           option = option.replace(systemName, "");
           option = option.replace("_", "");

           HTMLPathGenerator generator = new HTMLPathGenerator(root, systemName, option);
           generator.generateHTMLPage();
        }
    }

    @Override
    public void generateStaticHTMLPage(List<SourceToSinkPath> results) throws IOException {
        String[] extensions = {"java"};

        Collection<File> files = FileUtils.listFiles(new File(this.getRoot()), extensions, true);

        for(int r = 0; r < results.size(); r++) {
            List<SourceToSinkPath.PathElement> path = results.get(r).getPath();
            Map<String, List<SourceToSinkPath.PathElement>> filesToPathElements = new LinkedHashMap<>();

            for(SourceToSinkPath.PathElement pathElement : path) {
                String resultClassName = pathElement.getClassName();
                String resultFilePath = resultClassName.replace(".", "/") + ".java";

                for(File file : files) {
                    if(!file.getPath().contains(resultFilePath)) {
                        continue;
                    }

                    if(!filesToPathElements.containsKey(resultFilePath)) {
                        filesToPathElements.put(resultFilePath, new ArrayList<>());
                    }

                    List<SourceToSinkPath.PathElement> pathElements = filesToPathElements.get(resultFilePath);
                    pathElements.add(pathElement);

                    break;

                }
            }

            if(filesToPathElements.isEmpty()) {
                continue;
            }

            for(Map.Entry<String, List<SourceToSinkPath.PathElement>> fileToPathElements : filesToPathElements.entrySet()) {
                for(File file : files) {
                    if(!file.getPath().contains(fileToPathElements.getKey())) {
                        continue;
                    }

                    this.setHtmlTitle(this.option + "_" + file.getName());

                    StringBuilder staticHTMLPage = new StringBuilder();

                    FileInputStream fstream = new FileInputStream(this.getRoot() + file.getPath().replace(this.getRoot(), ""));
                    DataInputStream in = new DataInputStream(fstream);
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    String strLine;
                    int lineNumber = 1;

                    while ((strLine = br.readLine()) != null) {
                        boolean inPath = false;

                        for(SourceToSinkPath.PathElement pathElement : fileToPathElements.getValue()) {
                            if(pathElement.getJavaLineNumber() == lineNumber) {
                                inPath = true;
                                break;
                            }
                        }

                        if(inPath) {
                            staticHTMLPage.append("<div style=\"background-color:orange;\">");
                        }
                        else {
                            staticHTMLPage.append("<div>");
                        }

                        staticHTMLPage.append(lineNumber);
                        staticHTMLPage.append("&emsp;&emsp;");

                        for(int i = 0; !strLine.isEmpty() && strLine.charAt(i) == ' '; i++) {
                            staticHTMLPage.append("&nbsp;");
                        }

                        if(inPath) {
                            staticHTMLPage.append("<b>");
                            staticHTMLPage.append(strLine);
                            staticHTMLPage.append("&nbsp;");

                            for(int i = 0; i < fileToPathElements.getValue().size(); i++) {
                                SourceToSinkPath.PathElement pathElement = fileToPathElements.getValue().get(i);

                                if(pathElement.getJavaLineNumber() == lineNumber) {
                                    staticHTMLPage.append(" &#8594; ");
                                    staticHTMLPage.append(i);
                                }
                            }

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

                    File outputFile = new File(HTMLPathGenerator.ROOT_DIR + this.getSystemName() + "/"
                            + HTMLPathGenerator.PATH_DIR + "/" + this.option + "_" + r + "/" + file.getName() + ".html");
                    outputFile.getParentFile().mkdirs();
                    PrintWriter out = new PrintWriter(outputFile);
                    out.print(HTMLPathGenerator.HTML_HEAD_BEGIN + this.getHtmlTitle() + HTMLPathGenerator.HTML_HEAD_END
                            + HTMLPathGenerator.HTML_BODY + staticHTMLPage + HTMLPathGenerator.HTML_END);
                    out.flush();
                    out.close();

                    System.out.println("Generated " + file.getName() + "_" + r);
                }
            }
        }
    }

    @Override
    public List<SourceToSinkPath> readResults() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File inputFile = new File(TaintInfoflow.PATHS_DIR + "/" + this.getSystemName() + "/" + this.option + ".json");
        List<SourceToSinkPath> results = mapper.readValue(inputFile, new TypeReference<List<SourceToSinkPath>>() {
        });

        return results;
    }

}
