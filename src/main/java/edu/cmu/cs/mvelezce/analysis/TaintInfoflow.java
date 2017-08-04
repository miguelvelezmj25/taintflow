package edu.cmu.cs.mvelezce.analysis;

import soot.jimple.infoflow.Infoflow;
import soot.jimple.infoflow.solver.cfg.IInfoflowCFG;

public class TaintInfoflow extends Infoflow {

    public IInfoflowCFG getiCfg() { return this.iCfg; }
}
