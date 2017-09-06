package edu.cmu.cs.mvelezce.format.instrument.methodnode;

import edu.cmu.cs.mvelezce.format.instrument.classnode.ClassTransformerBase;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.Set;

public abstract class MethodTransformerBase extends ClassTransformerBase implements MethodTransformer {

    public MethodTransformerBase(String path) throws NoSuchMethodException, MalformedURLException, IllegalAccessException, InvocationTargetException {
        super(path);
    }

    @Override
    public void transformMethods() throws IOException {
        Set<ClassNode> classNodes = this.readClasses();
        this.transformMethods(classNodes);
    }

    @Override
    public void transformMethods(Set<ClassNode> classNodes) throws IOException {
        for(ClassNode classNode : classNodes) {
            Set<MethodNode> methodsToInstrument = this.getMethodsToInstrument(classNode);

            if(methodsToInstrument.isEmpty()) {
                continue;
            }

            System.out.println("Transforming class " + classNode.name);

            for(MethodNode methodToInstrument : methodsToInstrument) {
                this.transformMethod(methodToInstrument);
            }

            this.writeClass(classNode, this.getPath() + "/" + classNode.name);
        }
    }
}
