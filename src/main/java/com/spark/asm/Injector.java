package com.spark.asm;


import java.io.IOException;

import org.objectweb.asm.tree.ClassNode;

/**
 * Injector Provides an interface to modify an array of class nodes with additional
 * bytecode.
 *
 * @author Christian Bartram
 * @since 1.0
 */
public interface Injector {
    void modify(ClassNode[] nodes) throws IOException;
}
