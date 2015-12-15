package com.spark.lang;

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
 *
 * @author Ian
 * @version 1.0
 */
public class ClassCreator extends ClassLoader {
    private final Map<String, Class<?>> classes = new HashMap<>();
    private final Map<String, ClassNode> nodes = new HashMap<>();
    private final ProtectionDomain domain;
    private final Permissions permissions;
    private boolean allowReloading;

    public ClassCreator() {
        this(false);
    }

    public ClassCreator(boolean allowReloading) {
        this.allowReloading = allowReloading;
        Permissions permissions = new Permissions();
        permissions.add(new AllPermission());
        this.permissions = permissions;
        domain = new ProtectionDomain(new CodeSource(null, (Certificate[]) null), this.permissions);
    }

    public ClassCreator(ClassNode[] nodes) {
        this(nodes, false);
    }

    public ClassCreator(ClassNode[] nodes, boolean allowReloading) {
        this(allowReloading);
        if (nodes == null)
            return;
        for (ClassNode node : nodes)
            this.nodes.put(node.name, node);
    }

    public void add(ClassNode node) {
        if (node == null)
            return;
        nodes.put(node.name, node);
    }

    public void remove(ClassNode node) {
        if (node == null)
            return;
        nodes.remove(node.name);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return findClass(name);
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        if (name == null)
            throw new ClassNotFoundException();
        try {
            return getSystemClassLoader().loadClass(name);
        } catch (Exception e) {
            boolean reload = allowReloading();
            String key = name.replace('.', '/');
            if (!reload && classes.containsKey(key))
                return classes.get(key);
            ClassNode node = nodes.get(key);
            if (node == null)
                throw new ClassNotFoundException();
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            node.accept(cw);
            byte[] b = cw.toByteArray();
            Class<?> c = defineClass(node.name.replace('/', '.'), b, 0, b.length, getDomain());
            if (!reload)
                classes.put(key, c);
            return c;
        }
    }

    public ClassNode[] getNodes() {
        return nodes.values().toArray(new ClassNode[nodes.size()]);
    }

    public Class<?>[] getClasses() {
        return classes.values().toArray(new Class[classes.size()]);
    }

    public ProtectionDomain getDomain() {
        return domain;
    }

    public Permissions getPermissions() {
        return permissions;
    }

    public boolean allowReloading() {
        return allowReloading;
    }

    public void allowReloading(boolean allowReloading) {
        this.allowReloading = allowReloading;
    }
}