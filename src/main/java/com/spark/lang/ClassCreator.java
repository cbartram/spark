package com.spark.lang;

import java.security.AllPermission;
import java.security.CodeSource;
import java.security.Permissions;
import java.security.ProtectionDomain;
import java.security.cert.Certificate;
import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import lombok.Getter;

/**
 * ClassCreator
 * Returns a set of ClassNode objects which can be used by ASM by storing them
 * sequentially in a HashMap. It will then load a ClassNode as an actual Class
 * for the JVM to read and execute the reflection API on.
 * @author Christian Bartram
 * @since 1.0
 */
public class ClassCreator extends ClassLoader {
    private final Map<String, Class<?>> classes = new HashMap<>();
    private final Map<String, ClassNode> nodes = new HashMap<>();

    @Getter
    private final ProtectionDomain domain;

    @Getter
    private final Permissions permissions = new Permissions();

    public ClassCreator(ClassNode... nodes) {
        permissions.add(new AllPermission());
        domain = new ProtectionDomain(new CodeSource(null, (Certificate[]) null), this.permissions);
        if (nodes == null) return;
        for (ClassNode node : nodes)
            this.nodes.put(node.name, node);
    }

    /**
     * Adds a ClassNode to the list of classes
     * @param node ClassNode
     */
    public void add(ClassNode node) {
        if (node == null)
            return;
        String key = node.name.replace('.', '/');
        if (nodes.containsKey(key) && !node.equals(nodes.get(key)))
            classes.remove(key);
        nodes.put(node.name, node);
    }

    /**
     * Removes a classNode from the list of classes
     * @param node
     */
    public void remove(ClassNode node) {
        if (node == null)
            return;
        nodes.remove(node.name);
        classes.remove(node.name.replace('.', '/'));
    }

    public ClassNode[] getNodes() {
        return nodes.values().toArray(new ClassNode[nodes.size()]);
    }

    public Class<?>[] getClasses() {
        return classes.values().toArray(new Class[classes.size()]);
    }

    /**
     * Loads a class given the class name
     * @param name String the class name to search for and load
     * @return Class
     * @throws ClassNotFoundException
     */
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return findClass(name);
    }

    /**
     * Searches a list of Class Nodes for a specific class given the class name and attempts
     * to load the class
     * @param name String the class name to search for
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        if (name == null)
            throw new ClassNotFoundException();
        try {
            return getSystemClassLoader().loadClass(name);
        } catch (Exception e) {
            String key = name.replace('.', '/');
            if (classes.containsKey(key))
                return classes.get(key);
            ClassNode node = nodes.get(key);
            if (node == null)
                throw new ClassNotFoundException();
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            node.accept(cw);
            byte[] b = cw.toByteArray();
            Class<?> c = defineClass(node.name.replace('.', '/'), b, 0, b.length, this.domain);
            classes.put(key, c);
            return c;
        }
    }
}
