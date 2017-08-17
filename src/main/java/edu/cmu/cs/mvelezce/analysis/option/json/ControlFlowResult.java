package edu.cmu.cs.mvelezce.analysis.option.json;

import java.util.Set;

public class ControlFlowResult {

    private String packageName;
    private String className;
    private String methodSignature;
    private int bytecodeIndex;
    private int javaLine;
    private Set<String> options;
    private int optionCount;

    public ControlFlowResult() {
        ;
    }

    public ControlFlowResult(String packageName, String className, String methodSignature, int bytecodeIndex, int javaLine, Set<String> options) {
        this.packageName = packageName;
        this.className = className;
        this.methodSignature = methodSignature;
        this.bytecodeIndex = bytecodeIndex;
        this.javaLine = javaLine;
        this.options = options;
        this.optionCount = options.size();
    }

    public int getOptionCount() {
        return optionCount;
    }

    public void setOptionCount(int optionCount) {
        this.optionCount = optionCount;
    }

    public int getJavaLine() {
        return javaLine;
    }

    public void setJavaLine(int javaLine) {
        this.javaLine = javaLine;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodSignature() {
        return methodSignature;
    }

    public void setMethodSignature(String methodSignature) {
        this.methodSignature = methodSignature;
    }

    public int getBytecodeIndex() {
        return bytecodeIndex;
    }

    public void setBytecodeIndex(int bytecodeIndex) {
        this.bytecodeIndex = bytecodeIndex;
    }

    public Set<String> getOptions() {
        return options;
    }

    public void setOptions(Set<String> options) {
        this.options = options;
    }
}
