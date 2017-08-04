package edu.cmu.cs.mvelezce.analysis;

import soot.jimple.infoflow.Infoflow;
import soot.jimple.infoflow.solver.cfg.IInfoflowCFG;

import java.util.ArrayList;
import java.util.List;

public class TaintInfoflow extends Infoflow {

    private static List<String> sources = new ArrayList<>();
    private static List<String> sinks = new ArrayList<>();

    public IInfoflowCFG getiCfg() {
        return this.iCfg;
    }
}
