package com.spark.asm;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.security.AllPermission;
import java.security.CodeSource;
import java.security.Permissions;
import java.security.ProtectionDomain;
import java.security.cert.Certificate;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassCreator
 * Returns a set of ClassNode objects which can be used by ASM by storing them
 * sequentially in a HashMap. It will then load a ClassNode as an actual Class
 * for the JVM to read and execute the reflection API on.
 * @author Christian Bartram
 * @since 1.0
 */
@Slf4j
public class RunescapeClassLoader extends ClassLoader {

    @Getter
    private final Map<String, Class<?>> classes = new HashMap<>();

    @Getter
    private final Map<String, ClassNode> nodes = new HashMap<>();

    private final ProtectionDomain domain;

    public RunescapeClassLoader(ClassNode... nodes) {
        log.info("Loading Injected RuneScape classes...");
        final Permissions permissions = new Permissions();
        permissions.add(new AllPermission());
        domain = new ProtectionDomain(new CodeSource(null, (Certificate[]) null), permissions);
        if (nodes == null) return;
        for (ClassNode node : nodes) {
            this.nodes.put(node.name, node);
        }
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
        if (node == null) return;
        nodes.remove(node.name);
        classes.remove(node.name.replace('.', '/'));
    }

    /**
     * Searches a list of Class Nodes for a specific class given the class name and attempts
     * to load the class
     * @param name String the class name to search for
     * @return Class to load//
     * @throws ClassNotFoundException
     */
    @Override
    public Class<?> findClass(@NonNull final String name){
        try {
            return getSystemClassLoader().loadClass(name);
        } catch (ClassNotFoundException e) {
            log.debug("No class could be located for loading with the name: {}. Attempting to define and load class.", name);
            final String key = name.replace('.', '/');
            if (classes.containsKey(key))  return classes.get(key);
            ClassNode node = nodes.get(key);
            // Convert class node into actual class object which can be loaded and returned to the class loader.
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            node.accept(cw);
            byte[] b = cw.toByteArray();
            Class<?> c = defineClass(node.name.replace('.', '/'), b, 0, b.length, this.domain);
            classes.put(key, c);
            return c;
        }
    }
}
