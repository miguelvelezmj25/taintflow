package edu.cmu.cs.mvelezce.analysis.option.json;

import java.util.List;

public class SourceToSinkPath {

    private List<PathElement> path;

    public SourceToSinkPath(List<PathElement> path) {
        this.path = path;
    }

    public List<PathElement> getPath() {
        return path;
    }

    public void setPath(List<PathElement> path) {
        this.path = path;
    }

    public static class PathElement {
        private String className;
        private int javaLineNumber;

        public PathElement(String className, int javaLineNumber) {
            this.className = className;
            this.javaLineNumber = javaLineNumber;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public int getJavaLineNumber() {
            return javaLineNumber;
        }

        public void setJavaLineNumber(int javaLineNumber) {
            this.javaLineNumber = javaLineNumber;
        }
    }
}
