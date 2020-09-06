package com.spark.asm.transformer;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class ClientClassTransformer extends AbstractClassTransformer {

  @Override
  @SuppressWarnings("unchecked")
  public ClassNode transform(final ClassNode node) {
    node.interfaces.add("com/spark/asm/transformer/GraphicsInterface"); // TODO how does the class loader find this interface defined in MY code NOT the JAR file itself?
    MethodNode methodNode = new MethodNode(Opcodes.ACC_PUBLIC, "getCanvas", "()Ljava/awt/Canvas;", null, null);
    methodNode.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
    methodNode.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "class48", "field469", "Ljava/awt/Canvas;"));
    methodNode.instructions.add(new InsnNode(Opcodes.ARETURN));
    int size = methodNode.instructions.size();
    methodNode.visitMaxs(size, size);
    methodNode.visitEnd();
    node.methods.add(methodNode);
    return node;
  }
}
