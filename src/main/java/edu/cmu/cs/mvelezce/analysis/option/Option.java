package edu.cmu.cs.mvelezce.analysis.option;

public class Option {

    private final String name;
    private final String methodName;

    public Option(String name, String methodName) {
        this.name = name;
        this.methodName = methodName;
    }

    public String getName() {
        return this.name;
    }

    public String getMethodName() {
        return this.methodName;
    }
}
