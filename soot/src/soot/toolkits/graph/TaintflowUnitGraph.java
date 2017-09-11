package soot.toolkits.graph;

import soot.*;
import soot.jimple.internal.JIdentityStmt;
import soot.jimple.internal.JThrowStmt;
import soot.util.Chain;

import java.util.ArrayList;
import java.util.List;

public class TaintflowUnitGraph extends BriefUnitGraph {

    /**
     * Constructs a TaintflowUnitGraph given a Body instance.
     *
     * @param body The underlying body we want to make a
     *             graph for.
     */
    public TaintflowUnitGraph(Body body) {
        super(body);

        if(!body.getMethod().getDeclaringClass().isJavaLibraryClass()) {
            this.addEdgeThrowToCaught(body);
        }
    }

    public void addEdgeThrowToCaught(Body body) {
        if(body.getTraps().isEmpty()) {
            return;
        }


        PatchingChain<Unit> units = body.getUnits();

        for(Unit unit : units) {
            if(!(unit instanceof JThrowStmt)) {
                continue;
            }

            JThrowStmt throwStmt = (JThrowStmt) unit;
            Value op = throwStmt.getOp();
            RefType thrownType = (RefType) op.getType();
            SootClass thrownExceptionClass = thrownType.getSootClass();

            FastHierarchy fh = Scene.v().getFastHierarchy();
            Chain<Trap> traps = body.getTraps();

            for(Trap trap : traps) {
                Unit handlerUnit = trap.getHandlerUnit();

                if(!(handlerUnit instanceof JIdentityStmt)) {
                    throw new RuntimeException("We expect a JIdentityStmt, but got " + handlerUnit.getClass());
                }

                JIdentityStmt identityStmt = (JIdentityStmt) handlerUnit;
                RefType catchType = (RefType) identityStmt.leftBox.getValue().getType();
                SootClass catchExceptionClass = catchType.getSootClass();

                if(fh.isSubclass(thrownExceptionClass, catchExceptionClass)) {
                    if(this.getSuccsOf(unit).isEmpty()) {
                        List<Unit> succs = new ArrayList<>();
                        this.unitToSuccs.put(unit, succs);
                    }

                    this.getSuccsOf(unit).add(handlerUnit);

                    if(this.getPredsOf(handlerUnit).isEmpty()) {
                        List<Unit> preds = new ArrayList<>();
                        this.unitToPreds.put(handlerUnit, preds);
                    }

                    this.getPredsOf(handlerUnit).add(unit);
                }
            }
        }
    }
}
