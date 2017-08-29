package edu.cmu.cs.mvelezce.visualization;

import java.io.IOException;
import java.util.List;

public abstract class HTMLBaseGenerator<V> implements HTMLGenerator<V> {

    public static final String ROOT_DIR = "src/visualization/";

    private String htmlTitle = "Default";
    private String root;
    private String systemName;

    public HTMLBaseGenerator(String root, String systemName) {
        this.root = root;
        this.systemName = systemName;
    }

    @Override
    public void generateHTMLPage() throws IOException {
        List<V> results = this.readResults();
        this.generateStaticHTMLPage(results);
    }

    public static String replaceSpecialChars(String line) {
        line = line.replace("<", "&#60;");
        line = line.replace("<", "&#62;");

        return line;
    }

    public String getHtmlTitle() {
        return htmlTitle;
    }

    public void setHtmlTitle(String htmlTitle) {
        this.htmlTitle = htmlTitle;
    }

    public String getRoot() {
        return root;
    }

    public String getSystemName() {
        return systemName;
    }

}
