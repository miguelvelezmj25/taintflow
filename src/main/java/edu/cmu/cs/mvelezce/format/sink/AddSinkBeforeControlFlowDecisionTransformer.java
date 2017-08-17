package edu.cmu.cs.mvelezce.format.sink;

import edu.cmu.cs.mvelezce.format.instrument.methodnode.MethodTransformerBase;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Set;

public class AddSinkBeforeControlFlowDecisionTransformer extends MethodTransformerBase {

    public AddSinkBeforeControlFlowDecisionTransformer(String directory) throws NoSuchMethodException, MalformedURLException, IllegalAccessException, InvocationTargetException {
        super(directory);
    }

    @Override
    public void transformMethod(MethodNode methodNode) {
        System.out.println("Transforming method " + methodNode.name);

        InsnList instructions = methodNode.instructions;
        ListIterator<AbstractInsnNode> instructionsIterator = instructions.iterator();

        InsnList newInstructions = new InsnList();

        while (instructionsIterator.hasNext()) {
            AbstractInsnNode instruction = instructionsIterator.next();

            // TODO throws? table switch?
            if(instruction.getOpcode() >= Opcodes.LCMP && instruction.getOpcode() <= Opcodes.IF_ACMPNE) {
                MethodInsnNode methodInstructionNode = new MethodInsnNode(Opcodes.INVOKESTATIC, "edu/cmu/cs/mvelezce/analysis/option/Sink", "getDecision", "(Z)Z", false);
                newInstructions.add(methodInstructionNode);
            }

            newInstructions.add(instruction);
        }

        methodNode.instructions.clear();
        methodNode.instructions.add(newInstructions);

    }

    @Override
    public Set<MethodNode> getMethodsToInstrument(ClassNode classNode) {
        Set<MethodNode> methodsToInstrument = new HashSet<>();

        for(MethodNode methodNode : classNode.methods) {
            InsnList instructions = methodNode.instructions;
            ListIterator<AbstractInsnNode> instructionsIterator = instructions.iterator();

            while (instructionsIterator.hasNext()) {
                AbstractInsnNode instruction = instructionsIterator.next();

                // TODO throws? table switch?
                if(instruction.getOpcode() >= Opcodes.LCMP && instruction.getOpcode() <= Opcodes.IF_ACMPNE) {
                    methodsToInstrument.add(methodNode);
                    continue;
                }
            }
        }

        return methodsToInstrument;
    }
}
