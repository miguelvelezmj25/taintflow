package soot.jimple.infoflow;

import soot.*;
import soot.jimple.*;
import soot.jimple.internal.JimpleLocal;
import soot.util.Chain;

import java.util.*;

public class IfSink extends BodyTransformer {
    private static IfSink instance = new IfSink();

    private IfSink() {
        ;
    }

    public static IfSink v() {
        return IfSink.instance;
    }

    @Override
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        SootClass sootClass = Scene.v().loadClassAndSupport("edu.cmu.cs.mvelezce.analysis.option.Sink");
        SootMethod sootMethod = sootClass.getMethod("void sink(java.lang.Object)");

        System.out.println(b.getMethod().getSignature());
        Chain<Unit> units = b.getUnits();
        Chain<Local> locals = b.getLocals();
        List<Ins> inss = new ArrayList<>();

        for(Unit unit : units) {
//            System.out.println(unit);

            if(unit instanceof IfStmt) {
                IfStmt ifStmt = (IfStmt) unit;
                Value cond = ifStmt.getCondition();

                if(cond instanceof ConditionExpr) {
                    ConditionExpr condExpr = (ConditionExpr) cond;
                    Value op1 = condExpr.getOp1();
                    Value op2 = condExpr.getOp2();

                    if(locals.contains(op1)) {
                        Ins i = new Ins(op1, unit, op1.getType());
                        System.out.println("SINK " + op1 + " " + op1.getType());
                        inss.add(i);
                    }

                    if(locals.contains(op2)) {
                        Ins i = new Ins(op2, unit, op2.getType());
                        System.out.println("SINK " + op2 + " " + op2.getType());
                        inss.add(i);
                    }
                }
                else {
                    throw new RuntimeException("Other type of condition");
                }
            }
        }

        if(!inss.isEmpty()) {
            for(Ins ins : inss) {
                StaticInvokeExpr invExpr = Jimple.v().newStaticInvokeExpr(sootMethod.makeRef(), ins.variable);
//                Local local = Jimple.v().newLocal("tmp" + UUID.randomUUID(), BooleanType.v());
                Stmt stmt = Jimple.v().newInvokeStmt(invExpr);
                units.insertBefore(stmt, ins.u);
            }
        }

        System.out.println("");
    }

    public class Ins {
        Value variable;
        Unit u;
        Type t;

        public Ins(Value variable, Unit u, Type t) {
            this.variable = variable;
            this.u = u;
            this.t = t;
        }


    }
}
