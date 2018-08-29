package edu.cmu.cs.mvelezce.soot.jimple.jtp;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.BodyTransformer;
import soot.PatchingChain;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.Stmt;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JIfStmt;

public class AssignmentInControlFlowCounter extends BodyTransformer {

  private static final String TAG = AssignmentInControlFlowCounter.class.getSimpleName();
  private static final AssignmentInControlFlowCounter INSTANCE =
      new AssignmentInControlFlowCounter();

  private final Map<SootMethod, Integer> methodsToCounts = new HashMap<>();

  private int numTotalAssigns = 0;
  private int numAssignInsideControlFlows = 0;

  private AssignmentInControlFlowCounter() {}

  @Override
  protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
    SootMethod sootMethod = body.getMethod();
    initializeMethodInCounterMap(sootMethod);

    Set<Unit> controlFlowTargets = new LinkedHashSet<>();
    PatchingChain<Unit> units = body.getUnits();

    for (Unit unit : units) {
      controlFlowTargets.remove(unit);

      if (unit instanceof JIfStmt) {
        Stmt target = ((JIfStmt) unit).getTarget();
        controlFlowTargets.add(target);
      } else if (unit instanceof JAssignStmt) {
        numTotalAssigns++;

        if (!controlFlowTargets.isEmpty()) {
          numAssignInsideControlFlows++;
          int currentCount = methodsToCounts.get(sootMethod);
          methodsToCounts.put(sootMethod, currentCount + 1);
        }
      }
    }

    if (!controlFlowTargets.isEmpty()) {
      throw new RuntimeException(TAG + ": The control flow targets queue is not empty");
    }
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

  public int getNumTotalAssigns() { return numTotalAssigns; }

  public int getNumAssignInsideControlFlows() { return numAssignInsideControlFlows; }

  private void initializeMethodInCounterMap(SootMethod sootMethod) {
    if (methodsToCounts.containsKey(sootMethod)) {
      System.out.println(
          TAG + ": The method " + sootMethod.getName() + " is already present in the counter map");
    } else {
      methodsToCounts.put(sootMethod, 0);
    }
  }
}
