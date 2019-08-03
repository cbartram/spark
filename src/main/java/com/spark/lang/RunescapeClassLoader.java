package com.spark.lang;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.util.HashMap;

/**
 * Created by christianbartram on 1/13/18.
 * <p>
 * http://github.com/cbartram
 */
public class RunescapeClassLoader extends ClassLoader {
    private HashMap<String, ClassNode> nodes = new HashMap<>();

    public RunescapeClassLoader(HashMap<String, ClassNode> nodes) {
        this.nodes = nodes;
    }

    public Class<?> defineClass(String name, byte[] b) {
            return defineClass(name, b, 0, b.length);
    }

    @Override
    public Class findClass(String name) throws ClassNotFoundException {
        if (name == null) throw new ClassNotFoundException();
        ClassNode node = nodes.get(name);
        if (node == null) throw new ClassNotFoundException();
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(cw);
        byte[] b = cw.toByteArray();
        return defineClass(node.name, b, 0, b.length);
    }
}