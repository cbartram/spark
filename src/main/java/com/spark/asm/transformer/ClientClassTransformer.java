package com.spark.asm.transformer;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;

public class ClientClassTransformer extends AbstractClassTransformer {

  @Override
  @SuppressWarnings("unchecked")
  public ClassNode transform(final ClassNode node) {
    MethodNode methodNode = new MethodNode(Opcodes.ACC_PUBLIC, "simpleMethod", "()V", null, null);
    methodNode.instructions.add(new InsnNode(Opcodes.RETURN));

    // May not be needed
    int size = methodNode.instructions.size();
    methodNode.visitMaxs(size, size);
    methodNode.visitEnd();

    node.methods.add(methodNode);
    return node;
  }
}
