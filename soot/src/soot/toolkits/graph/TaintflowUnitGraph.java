package soot.toolkits.graph;

import soot.Body;
import soot.PatchingChain;
import soot.Trap;
import soot.Unit;
import soot.jimple.NopStmt;
import soot.jimple.internal.JIdentityStmt;
import soot.jimple.internal.JThrowStmt;

import java.util.ArrayList;
import java.util.Iterator;
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

            for(Trap trap : body.getTraps()) {
                Unit handlerUnit = trap.getHandlerUnit();

                if(handlerUnit instanceof NopStmt) {
                    handlerUnit = units.getSuccOf(handlerUnit);
                }

                if(!(handlerUnit instanceof JIdentityStmt)) {
                    throw new RuntimeException("We expect a JIdentityStmt, but got " + handlerUnit.getClass());
                }

                Unit beginUnit = trap.getBeginUnit();
                Unit endUnit = trap.getEndUnit();

                Iterator<Unit> unitsIter = body.getUnits().iterator();
                boolean handled = false;

                while (unitsIter.hasNext()) {
                    Unit candidateBegin = unitsIter.next();

                    if(candidateBegin == unit || candidateBegin == endUnit) {
                        break;
                    }

                    if(candidateBegin == beginUnit) {
                        handled = true;
                        break;
                    }
                }

                if(!handled) {
                    continue;
                }

                handled = false;

                while (unitsIter.hasNext()) {
                    Unit candidateHandled = unitsIter.next();

                    if(candidateHandled == endUnit) {
                        break;
                    }

                    if(candidateHandled == unit) {
                        handled = true;
                        break;
                    }

                }

                if(!handled) {
                    continue;
                }

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
