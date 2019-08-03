package com.spark.util;


import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;

/**
 * Injector Provides an interface to modify an array of class nodes.
 *
 * @author Christian Bartram
 * @since 1.0
 */
public interface Injector {
    public void modify(ClassNode[] nodes) throws IOException;
}
