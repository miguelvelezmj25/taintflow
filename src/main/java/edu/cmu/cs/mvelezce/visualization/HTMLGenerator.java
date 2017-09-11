package edu.cmu.cs.mvelezce.visualization;

import java.io.IOException;
import java.util.List;

public interface HTMLGenerator<T> {

    public void generateHTMLPage() throws IOException;

    public void generateStaticHTMLPage(List<T> results) throws IOException;

    public List<T> readResults() throws IOException;
}
