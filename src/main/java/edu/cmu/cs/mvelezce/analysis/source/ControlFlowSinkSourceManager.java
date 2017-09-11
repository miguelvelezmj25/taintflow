package edu.cmu.cs.mvelezce.analysis.source;

import soot.jimple.IfStmt;
import soot.jimple.Stmt;
import soot.jimple.SwitchStmt;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.data.AccessPath;
import soot.jimple.infoflow.source.DefaultSourceSinkManager;

import java.util.ArrayList;
import java.util.Collection;

public class ControlFlowSinkSourceManager extends DefaultSourceSinkManager {

    public ControlFlowSinkSourceManager(Collection<String> sources) {
        this(sources, new ArrayList<>());
    }

    public ControlFlowSinkSourceManager(Collection<String> sources, Collection<String> sinks) {
        super(sources, sinks);
    }

    @Override
    public boolean isSink(Stmt sCallSite, InfoflowManager manager, AccessPath ap) {
        boolean isSink = super.isSink(sCallSite, manager, ap);

        if(isSink) {
            return isSink;
        }

        if(sCallSite instanceof IfStmt) {
            IfStmt ifStmt = (IfStmt) sCallSite;
            return true;
        }

        if(sCallSite instanceof SwitchStmt) {
            SwitchStmt switchStmt = (SwitchStmt) sCallSite;
            return true;
        }

        return false;
    }
}
