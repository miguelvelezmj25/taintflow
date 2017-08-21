package soot.jimple.infoflow;

import soot.*;
import soot.jimple.*;
import soot.tagkit.BytecodeOffsetTag;
import soot.tagkit.LineNumberTag;
import soot.tagkit.Tag;
import soot.util.Chain;

import java.util.HashMap;
import java.util.Map;

public class ControlFlowSink extends BodyTransformer {
    private static ControlFlowSink instance = new ControlFlowSink();

    private ControlFlowSink() {
        ;
    }

    public static ControlFlowSink v() {
        return ControlFlowSink.instance;
    }

    @Override
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        SootClass sootClass = Scene.v().loadClassAndSupport("edu.cmu.cs.mvelezce.analysis.option.Sink");
        SootMethod sootMethod = sootClass.getMethod("void sink(java.lang.Object)");

        Chain<Unit> units = b.getUnits();
        Chain<Local> locals = b.getLocals();
//        List<Ins> inss = new ArrayList<>();
        Map<Value, Unit> valuesToUnits = new HashMap<>();

        for(Unit unit : units) {
            if(unit instanceof IfStmt) {
                IfStmt ifStmt = (IfStmt) unit;
                Value cond = ifStmt.getCondition();

                if(cond instanceof ConditionExpr) {
                    ConditionExpr condExpr = (ConditionExpr) cond;
                    Value op1 = condExpr.getOp1();
                    Value op2 = condExpr.getOp2();

                    if(locals.contains(op1)) {
                        valuesToUnits.put(op1, unit);
                    }

                    if(locals.contains(op2)) {
                        valuesToUnits.put(op2, unit);
                    }
                }
                else {
                    throw new RuntimeException("Other type of condition");
                }
            }
            else if(unit instanceof SwitchStmt) {
                throw new RuntimeException("Need to implement this type of control flow decision");
            }
        }

        if(valuesToUnits.isEmpty()) {
            return;
        }

        for(Map.Entry<Value, Unit> valueToUnit : valuesToUnits.entrySet()) {
            StaticInvokeExpr invExpr = Jimple.v().newStaticInvokeExpr(sootMethod.makeRef(), valueToUnit.getKey());
            Stmt stmt = Jimple.v().newInvokeStmt(invExpr);

            Tag lineNumberTag = null;
            Tag bytecodeOffsetTag = null;

            for(Tag tag : valueToUnit.getValue().getTags()) {
                if(tag instanceof LineNumberTag) {
                    int javaLine = ((LineNumberTag) tag).getLineNumber();
                    lineNumberTag = new LineNumberTag(javaLine);
                }

                if(tag instanceof BytecodeOffsetTag) {
                    int bytecodeIndex = ((BytecodeOffsetTag) tag).getBytecodeOffset();
                    bytecodeOffsetTag = new BytecodeOffsetTag(bytecodeIndex);
                }
            }

            if(lineNumberTag == null) {
                lineNumberTag = new LineNumberTag(-1);
            }

            if(bytecodeOffsetTag == null) {
                bytecodeOffsetTag = new BytecodeOffsetTag(-1);
            }

            stmt.addTag(lineNumberTag);
            stmt.addTag(bytecodeOffsetTag);

            units.insertBefore(stmt, valueToUnit.getValue());
        }

    }

    public class Ins {
        Value variable;
        Unit u;

        public Ins(Value variable, Unit u) {
            this.variable = variable;
            this.u = u;
        }


    }
}
