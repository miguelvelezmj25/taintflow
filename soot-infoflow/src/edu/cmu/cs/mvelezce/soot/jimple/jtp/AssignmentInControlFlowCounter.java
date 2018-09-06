package edu.cmu.cs.mvelezce.soot.jimple.jtp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import javax.annotation.Nullable;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import soot.Body;
import soot.BodyTransformer;
import soot.PatchingChain;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.Stmt;
import soot.jimple.infoflow.solver.cfg.IInfoflowCFG;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JIfStmt;
import soot.toolkits.graph.BriefUnitGraph;

public class AssignmentInControlFlowCounter extends BodyTransformer {

  private static final String TAG = AssignmentInControlFlowCounter.class.getSimpleName();
  private static final AssignmentInControlFlowCounter INSTANCE =
      new AssignmentInControlFlowCounter();

  private final Map<SootMethod, Integer> methodsToCounts = new HashMap<>();

  private int numTotalAssigns = 0;
  private int numAssignInsideControlFlows = 0;
  private IInfoflowCFG taintAnalysisGraph;

  private AssignmentInControlFlowCounter() {}

  public void setTaintAnalysisGraph(IInfoflowCFG taintAnalysisGraph) {
    this.taintAnalysisGraph = taintAnalysisGraph;
  }

  @Override
  protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
    BriefUnitGraph unitGraph = new BriefUnitGraph(body);
    //    SootMethod sootMethod = body.getMethod();
    //    initializeMethodInCounterMap(sootMethod);

    PatchingChain<Unit> units = body.getUnits();
    Set<Pair<Stmt, Stmt>> controlFlowDecisions = getControlFlowDecisions(units);
    List<Stmt> stmtsInControlFlowDecisions =
        getStmtsInControlFlowDecisions(unitGraph, controlFlowDecisions);
    countAssignStmtsInControlFlowDecisions(stmtsInControlFlowDecisions);
    countTotalAssignStmts(units);
  }

  private void countTotalAssignStmts(PatchingChain<Unit> units) {
    for (Unit unit : units) {
      if (unit instanceof JAssignStmt) {
        numTotalAssigns++;
      }
    }
  }

  private void countAssignStmtsInControlFlowDecisions(List<Stmt> stmtsInControlFlowDecisions) {
    Set<Stmt> assignsInControlFlowDecisions = new HashSet<>();

    for (Stmt stmt : stmtsInControlFlowDecisions) {
      if (stmt instanceof JAssignStmt) {
        assignsInControlFlowDecisions.add(stmt);
      }
    }

    numAssignInsideControlFlows += assignsInControlFlowDecisions.size();
  }

  private List<Stmt> getStmtsInControlFlowDecisions(
      BriefUnitGraph unitGraph, Set<Pair<Stmt, Stmt>> controlFlowDecisions) {
    List<Stmt> stmtsInControlFlowDecisions = new ArrayList<>();

    for (Pair<Stmt, Stmt> controlFlowDecision : controlFlowDecisions) {
      Stmt start = controlFlowDecision.getLeft();
      Stmt target = controlFlowDecision.getRight();

      if (isRegularControlFlowDecision(unitGraph, start, target)) {
        Set<Stmt> analyzied = new HashSet<>();
        List<Unit> succs = new LinkedList<>(unitGraph.getSuccsOf(start));

        while (!succs.isEmpty()) {
          Stmt stmt = (Stmt) succs.remove(0);

          if (stmt.equals(target) || analyzied.contains(stmt)) {
            continue;
          }

          analyzied.add(stmt);
          stmtsInControlFlowDecisions.add(stmt);
          succs.addAll(unitGraph.getSuccsOf(stmt));
        }
      } else {
        List<Unit> succs = unitGraph.getSuccsOf(target);
//        Stmt stmt = target;
//
//        for (int i = 0; !stmt.equals(start); i++) {
//          stmt = (Stmt) succs.get(i);
//          stmtsInControlFlowDecisions.add(stmt);
//        }
      }
    }

    return stmtsInControlFlowDecisions;
  }

  private boolean isRegularControlFlowDecision(BriefUnitGraph unitGraph, Stmt start, Stmt target) {
    List<Unit> succs = unitGraph.getSuccsOf(start);
    return succs.contains(target);
  }

  private boolean containsControlFlowDecisionWithTarget(
      Stmt stmt, Queue<Pair<Stmt, Stmt>> curControlFlowDecisions) {
    for (Pair<Stmt, Stmt> pair : curControlFlowDecisions) {
      if (pair.getRight().equals(stmt)) {
        return true;
      }
    }

    return false;
  }

  private boolean isStartOrTargetOfCurControlFlowDecision(
      Stmt stmt, Queue<Pair<Stmt, Stmt>> curControlFlowDecisions) {
    for (Pair<Stmt, Stmt> controlFlowDecision : curControlFlowDecisions) {
      if (controlFlowDecision.getLeft().equals(stmt)
          || controlFlowDecision.getRight().equals(stmt)) {
        return true;
      }
    }

    return false;
  }

  private boolean isTargetOfControlFlowDecision(
      Stmt stmt, Set<Pair<Stmt, Stmt>> controlFlowDecisions) {
    for (Pair<Stmt, Stmt> controlFlowDecision : controlFlowDecisions) {
      if (controlFlowDecision.getRight().equals(stmt)) {
        return true;
      }
    }

    return false;
  }

  private boolean isStartOfControlFlowDecision(
      Stmt stmt, Set<Pair<Stmt, Stmt>> controlFlowDecisions) {
    for (Pair<Stmt, Stmt> controlFlowDecision : controlFlowDecisions) {
      if (controlFlowDecision.getLeft().equals(stmt)) {
        return true;
      }
    }

    return false;
  }

  @Nullable
  private Pair<Stmt, Stmt> getControlFlowDecision(
      Stmt stmt, Stmt target, Set<Pair<Stmt, Stmt>> controlFlowDecisions) {

    for (Pair<Stmt, Stmt> controlFlowDecision : controlFlowDecisions) {
      if (controlFlowDecision.getLeft().equals(stmt)
          && controlFlowDecision.getRight().equals(target)) {
        return controlFlowDecision;
      }
    }

    return null;
  }

  private boolean isStartOrTargetOfControlFlowDecision(
      Stmt stmt, Set<Pair<Stmt, Stmt>> controlFlowDecisions) {
    for (Pair<Stmt, Stmt> controlFlowDecision : controlFlowDecisions) {
      if (controlFlowDecision.getLeft().equals(stmt)
          || controlFlowDecision.getRight().equals(stmt)) {
        return true;
      }
    }

    return false;
  }

  private Set<Pair<Stmt, Stmt>> getControlFlowDecisions(PatchingChain<Unit> units) {
    Set<Pair<Stmt, Stmt>> controlFlowDecisions = new HashSet<>();

    for (Unit unit : units) {
      if (!(unit instanceof JIfStmt)) {
        continue;
      }

      Unit postDominator = taintAnalysisGraph.getPostdominatorOf(unit).getUnit();
      Pair<Stmt, Stmt> controlFlowDecision =
          new ImmutablePair<>(((Stmt) unit), ((Stmt) postDominator));
      controlFlowDecisions.add(controlFlowDecision);
    }

    return controlFlowDecisions;
  }

  public static AssignmentInControlFlowCounter getInstance() {
    return INSTANCE;
  }

  //  public Map<SootMethod, Integer> getMethodsToCounts() {
  //    return methodsToCounts;
  //  }
  //
  //  public void printMethodsToCounts() {
  //    for (Map.Entry<SootMethod, Integer> entry : methodsToCounts.entrySet()) {
  //      System.out.println(entry.getKey() + " - " + entry.getValue());
  //    }
  //  }
  //
  //  public int getTotalCount() {
  //    int total = 0;
  //
  // for (Map.Entry<SootMethod, Integer> entry : methodsToCounts.entrySet()) {
  //      total += entry.getValue();
  //    }
  //
  //    return total;
  //  }

  public int getNumTotalAssigns() {
    return numTotalAssigns;
  }

  public int getNumAssignInsideControlFlows() {
    return numAssignInsideControlFlows;
  }

  private void initializeMethodInCounterMap(SootMethod sootMethod) {
    if (methodsToCounts.containsKey(sootMethod)) {
      System.out.println(
          TAG + ": The method " + sootMethod.getName() + " is already present in the counter map");
    } else {
      methodsToCounts.put(sootMethod, 0);
    }
  }
}
