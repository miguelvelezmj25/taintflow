package edu.cmu.cs.mvelezce.soot.jimple.jtp;

import soot.*;
import soot.jimple.Jimple;
import soot.jimple.NopStmt;
import soot.tagkit.BytecodeOffsetTag;
import soot.tagkit.LineNumberTag;
import soot.tagkit.Tag;
import soot.util.Chain;

import java.util.Iterator;
import java.util.Map;

public class Nop extends BodyTransformer {
    private static Nop instance = new Nop();

    private Nop() {
        ;
    }

    public static Nop v() {
        return Nop.instance;
    }

    @Override
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        PatchingChain<Unit> units = b.getUnits();
        Iterator<Unit> unitsIterator = b.getUnits().snapshotIterator();

        while(unitsIterator.hasNext()) {
            Unit unit = unitsIterator.next();

            Tag lineNumberTag = null;
            Tag bytecodeOffsetTag = null;

            for(Tag tag : unit.getTags()) {
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

            NopStmt nop = Jimple.v().newNopStmt();
            nop.addTag(lineNumberTag);
            nop.addTag(bytecodeOffsetTag);

            units.insertBefore(nop, unit);
        }
    }
}