package com.spark.util;

import org.objectweb.asm.tree.ClassNode;

/**
 * Injector
 *
 * @author Ian
 * @version 1.0
 */
public interface Injector {
    public void modify(ClassNode[] nodes);
}
