package com.spark.asm.transformer;

import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Iterator;


@Slf4j
public class AppletClassTransformer extends AbstractClassTransformer {

  @Override
  @SuppressWarnings("unchecked")
  public ClassNode transform(final ClassNode node) {
    node.interfaces.add("com/spark/asm/transformer/GraphicsInterface");
    MethodNode methodNode = new MethodNode(Opcodes.ACC_PUBLIC, "getCanvas", "()Ljava/awt/Canvas;", null, null);
    methodNode.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
    methodNode.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "class48", "field469", "Ljava/awt/Canvas;"));
    methodNode.instructions.add(new InsnNode(Opcodes.ARETURN));
    int size = methodNode.instructions.size();
    methodNode.visitMaxs(size, size);
    methodNode.visitEnd();
    node.methods.add(methodNode);


    // Modify constructor?
    node.superName = "com/spark/asm/transformer/BotApplet";
    // Loop through every method node
    for (MethodNode mNode : (Iterable<MethodNode>) node.methods) {
      if (mNode.name.equals("<init>")) {
        Iterator<AbstractInsnNode> instructs = mNode.instructions.iterator();
        while (instructs.hasNext()) {
          AbstractInsnNode ain = (AbstractInsnNode) instructs.next();
          // INVOKESPECIAL is basically super() in a constructor
          if (ain.getOpcode() == Opcodes.INVOKESPECIAL) {
            //cast to MethodInsnNode so we can change the "owner" variable to our own
            MethodInsnNode min = (MethodInsnNode) ain;

            // Instead of calling Runescape's super class call our bot superclass
//            min.owner = "com/spark/asm/transformer/BotApplet";
          }
        }
      }
    }
    return node;
  }
}
