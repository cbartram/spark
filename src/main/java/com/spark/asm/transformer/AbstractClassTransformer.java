package com.spark.asm.transformer;

import org.objectweb.asm.tree.ClassNode;

public abstract class AbstractClassTransformer {
  public abstract ClassNode transform(ClassNode node);
}
