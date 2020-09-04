package com.spark.asm;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

import java.io.IOException;
import java.security.AllPermission;
import java.security.CodeSource;
import java.security.Permissions;
import java.security.ProtectionDomain;
import java.security.cert.Certificate;
import java.util.*;

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

    public Class<?> findOnClassPath(@NonNull final String name) throws ClassNotFoundException {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

        List<Class<?>> candidates = new ArrayList<>();
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders("com.spark")) + "/**/*.class";
        try {
            final Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
            for (Resource resource : resources) {
                if (resource.isReadable()) {
                    final MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                    Class<?> c = Class.forName(metadataReader.getClassMetadata().getClassName());
                    final String className = c.getName().substring(c.getName().lastIndexOf(".") + 1);
                    log.debug("Evaluating loaded class: {} against specified criteria: {}", className, name);
                    if (className.equalsIgnoreCase(name)) {
                        candidates.add(c);
                    }
                }
            }
        } catch(IOException e) {
            log.error("IOException thrown while searching through base package: com.spark to find class with name: {}", name, e);
            throw new ClassNotFoundException("IOException thrown while searching through base package: com.spark to find class with name: " + name);
        }

        log.info("Found {} class(s) on class path which match the criteria \"name={}\" while searching through base package: {}", candidates.size(), name, "com.spark");
        if(candidates.size() > 0) {
            return candidates.get(0);
        } else {
            throw new ClassNotFoundException("No class with the name: " + name + " could be found on the classpath searching through base package: com.spark");
        }
    }

    /**
     * Searches a list of Class Nodes for a specific class given the class name and attempts
     * to load the class
     * @param name String the class name to search for
     * @return Class to load
     * @throws ClassNotFoundException
     */
    @Override
    public Class<?> findClass(@NonNull final String name) throws ClassNotFoundException {
        try {
            log.info("Attempting to load class with name: {}", name);
            return getSystemClassLoader().loadClass(name);
        } catch (ClassNotFoundException e) {
            log.info("No class could be located for loading with the name: {}. Attempting to define and load class.", name);
            final String key = name.replace('.', '/');
            if (classes.containsKey(key)) return classes.get(key);

            ClassNode node = nodes.get(key);
            log.info("Key: {}", key);
            if (node == null) {
                // Scan through class path looking for the class before giving up.
                // This will usually happen when we are modifying the RS JAR classes with custom interfaces
                // that we define on our class path
                log.info("No class node found for node: {}", key);
                Class<?> c = findOnClassPath(key);
                classes.put(key, c);
                return c;
            }
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            node.accept(cw);
            byte[] b = cw.toByteArray();
            log.info("Node name: {}", node.name);
            Class<?> c = defineClass(node.name.replace('.', '/'), b, 0, b.length, this.domain);
            classes.put(key, c);
            return c;
        }
    }
}
