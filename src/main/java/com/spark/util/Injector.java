package com.spark.util;

import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;

/**
 * Injector
 *
 * @author Ian Caffey
 * @since 1.0
 */
public interface Injector {
    public void modify(ClassNode[] nodes) throws IOException;
}
