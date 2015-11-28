package com.spark.asm;

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
 * ASMClassLoader
 *
 * @author Ian
 * @version 1.0
 */
public class ASMClassLoader extends ClassLoader {
    private final Map<String, Class<?>> classes = new HashMap<>();
    private final Map<String, ClassNode> nodes = new HashMap<>();
    private final ProtectionDomain domain;
    private final Permissions permissions;
    private boolean allowReloading;

    public ASMClassLoader() {
        this(false);
    }

    public ASMClassLoader(boolean allowReloading) {
        this.allowReloading = allowReloading;
        Permissions permissions = new Permissions();
        permissions.add(new AllPermission());
        this.permissions = permissions;
        domain = new ProtectionDomain(new CodeSource(null, (Certificate[]) null), this.permissions);
    }

    public ASMClassLoader(ClassNode[] nodes) {
        this(nodes, false);
    }

    public ASMClassLoader(ClassNode[] nodes, boolean allowReloading) {
        this(allowReloading);
        if (nodes == null)
            return;
        for (ClassNode node : nodes)
            this.nodes.put(node.name, node);
    }

    protected byte[] nodeToBytes(ClassNode node) {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(cw);
        return cw.toByteArray();
    }

    protected Class<?> nodeToClass(ClassNode node) {
        byte[] b = nodeToBytes(node);
        return defineClass(node.name.replace('/', '.'), b, 0, b.length,
                getDomain());
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
            String key = name.replace('.', '/');
            if (!allowReloading() && classes.containsKey(key))
                return classes.get(key);
            ClassNode node = nodes.get(key);
            if (node == null)
                throw new ClassNotFoundException();
            Class<?> c = nodeToClass(node);
            classes.put(key, c);
            return c;
        }
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