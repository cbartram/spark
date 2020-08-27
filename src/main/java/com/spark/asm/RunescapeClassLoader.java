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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    public void findOnClassPath(@NonNull final String name) throws ClassNotFoundException, IOException {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

        Set<Class<?>> candidates = new HashSet<>();
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders("com.spark")) + "/**/*.class";
        Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
        for (Resource resource : resources) {
            if (resource.isReadable()) {
                MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                try {
                    Class<?> c = Class.forName(metadataReader.getClassMetadata().getClassName());
                    final String className = c.getName().substring(c.getName().lastIndexOf(".") + 1);
                    log.debug("Evaluating loaded class: {} against specified criteria: {}", className, name);
                    if(className.equalsIgnoreCase(name))  {
                        candidates.add(Class.forName(metadataReader.getClassMetadata().getClassName()));
                    }
                } catch(Throwable e) {
                    e.printStackTrace();
                }
            }
        }

        log.info("Found {} class(s) which match criteria: {} searching through base package: {}", candidates.size(), name, "com.spark");
        if(candidates.size() > 0) {

        } else {
            throw new ClassNotFoundException("No class with the name: " + name + " could be found on the classpath searching through base package: com.spark");
        }
    }

    /**
     * Searches a list of Class Nodes for a specific class given the class name and attempts
     * to load the class
     * @param name String the class name to search for
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    public Class<?> findClass(@NonNull final String name) throws ClassNotFoundException {
        try {
            log.debug("Attempting to load class with name: {}", name);
            return getSystemClassLoader().loadClass(name);
        } catch (ClassNotFoundException e) {
            log.debug("No class could be located for loading with the name: {}. Attempting to define and load class.", name);
            final String key = name.replace('.', '/');
            if (classes.containsKey(key))
                return classes.get(key);
            ClassNode node = nodes.get(key);
            if (node == null) {
                // TODO scan through class path looking for this class before giving up.
                try {
                    findOnClassPath(key);
                } catch(IOException ex) {
                    ex.printStackTrace();
                }
                throw new ClassNotFoundException("No class could be found for the key: " + key);
            }

//            log.info("Node name: {}", node.name);
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            node.accept(cw);
            byte[] b = cw.toByteArray();
//            log.info("Byte array: {}", b);
            Class<?> c = defineClass(node.name.replace('.', '/'), b, 0, b.length, this.domain);
            classes.put(key, c);
            return c;
        }
    }
}
