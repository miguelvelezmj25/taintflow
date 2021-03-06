package edu.cmu.cs.mvelezce.format.sink;

import edu.cmu.cs.mvelezce.format.instrument.classnode.ClassTransformer;
import edu.cmu.cs.mvelezce.format.instrument.classnode.DefaultBaseClassTransformer;
import edu.cmu.cs.mvelezce.format.instrument.methodnode.BaseMethodTransformer;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Set;


public class AddSinkBeforeControlFlowDecisionTransformerMethodTransformer extends BaseMethodTransformer {

    public AddSinkBeforeControlFlowDecisionTransformerMethodTransformer(String directory) throws NoSuchMethodException, MalformedURLException, IllegalAccessException, InvocationTargetException {
        ClassTransformer classTransformer = new DefaultBaseClassTransformer(directory);
        this.setClassTransformer(classTransformer);
    }

    @Override
    public void transformMethod(MethodNode methodNode) {
        System.out.println("Transforming method " + methodNode.name);

        InsnList instructions = methodNode.instructions;
        ListIterator<AbstractInsnNode> instructionsIterator = instructions.iterator();

        InsnList newInstructions = new InsnList();

        while (instructionsIterator.hasNext()) {
            AbstractInsnNode instruction = instructionsIterator.next();
            int opcode = instruction.getOpcode();


            if(opcode >= Opcodes.IF_ICMPEQ && opcode <= Opcodes.IF_ACMPNE) {
//                MethodInsnNode methodInstructionNode = new MethodInsnNode(Opcodes.INVOKESTATIC, "edu/cmu/cs/mvelezce/analysis/option/Sink", "getDecision", "(I)I", false);
//                AbstractInsnNode lastInstruction = newInstructions.getLast();
//                newInstructions.insertBefore(lastInstruction, methodInstructionNode);

                InsnNode insnNode = new InsnNode(Opcodes.DUP);
                MethodInsnNode methodInstructionNode = new MethodInsnNode(Opcodes.INVOKESTATIC, "edu/cmu/cs/mvelezce/analysis/option/Sink", "sink", "(Z)V", false);

                AbstractInsnNode lastInstruction = newInstructions.getLast();
                newInstructions.insertBefore(lastInstruction, methodInstructionNode);
            }
            // TODO Execptions?
            if(opcode >= Opcodes.IFEQ && opcode <= Opcodes.IF_ACMPNE || opcode == Opcodes.TABLESWITCH
                    || opcode == Opcodes.LOOKUPSWITCH || opcode == Opcodes.IFNULL || opcode == Opcodes.IFNONNULL) {
                MethodInsnNode methodInstructionNode;

//                if(opcode == Opcodes.IFNULL || opcode == Opcodes.IFNONNULL) {
//                    methodInstructionNode = new MethodInsnNode(Opcodes.INVOKESTATIC, "edu/cmu/cs/mvelezce/analysis/option/Sink", "getDecision", "(Ljava/lang/Object;)Ljava/lang/Object;", false);
//                }
//                else {
//
//                if(newInstructions.getLast().getType() == AbstractInsnNode.FIELD_INSN) {
                InsnNode insnNode = new InsnNode(Opcodes.DUP);
                methodInstructionNode = new MethodInsnNode(Opcodes.INVOKESTATIC, "edu/cmu/cs/mvelezce/analysis/option/Sink", "sink", "(Z)V", false);
//                    methodInstructionNode = new MethodInsnNode(Opcodes.INVOKESTATIC, "edu/cmu/cs/mvelezce/analysis/option/Sink", "getDecision", "(I)I", false);
//                }
//                else {
//                    MethodInsnNode methodInstructionNode = new MethodInsnNode(Opcodes.INVOKESTATIC, "edu/cmu/cs/mvelezce/analysis/option/Sink", "getDecision", "(I)I", false);
//                    newInstructions.add(methodInstructionNode);
//                }
                newInstructions.add(insnNode);
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
                int opcode = instruction.getOpcode();

                // TODO Execptions?
                if(opcode >= Opcodes.IFEQ && opcode <= Opcodes.IF_ACMPNE || opcode == Opcodes.TABLESWITCH
                        || opcode == Opcodes.LOOKUPSWITCH || opcode == Opcodes.IFNULL || opcode == Opcodes.IFNONNULL) {
                    methodsToInstrument.add(methodNode);
                    continue;
                }
            }
        }

        return methodsToInstrument;
    }
}
