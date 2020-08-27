package com.spark.util;

import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

/**
 * JarUtils - A Collection of useful utilities for reading and writing from Jar files.
 * @author Christian Bartram
 * @since 1.0
 */
@Slf4j
public class JarUtils {

  /**
   * Returns a set of actual Java Classes using reflection. Note: This will NOT return
   * a set of use-able classNodes for ASM injection.
   * @return Class[]
   * @param stream InputStream input stream to retrieve the JAR archive from.
   * @throws IOException Exception thrown when the stream is not valid or cannot be converted into a JAR input stream.
   */
  public Class<?>[] readClasses(final InputStream stream) throws IOException {
    if (stream == null) return new Class[0];

    JarInputStream jis = new JarInputStream(stream);
    List<Class<?>> classes = new ArrayList<>();
    JarEntry entry;

    while ((entry = jis.getNextJarEntry()) != null) {
      String name = entry.getName();

      if (!name.endsWith(".class")) continue;
      final String parsedClassName = name.replace('/', '.').substring(0, name.length() - 6);

      try {
        classes.add(Class.forName(parsedClassName));
      } catch (ClassNotFoundException e) {
        log.error("ClassNotFoundException thrown while attempting to read classes from Jar input stream. No class could be found for name: {}", parsedClassName, e);
      }
    }
    return classes.toArray(new Class[0]);
  }

  /**
   * Saves a map of ASM class Node objects to a usable JAR file.
   * @param classes Map Key is a string value of each node name and the value is the actual ClassNode object
   * @param outputPath String the output path where the JAR file should be written
   */
  public static void save(Map<String, ClassNode> classes, final String outputPath) {
    try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(outputPath), new Manifest())) {
      Collection<ClassNode> classNodes = classes.values();
      List<String> names = new ArrayList<>();

      for (ClassNode node : classNodes) {
        if (names.contains(node.name)) continue;

        JarEntry newEntry = new JarEntry(node.name + ".class");
        jos.putNextEntry(newEntry);

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        jos.write(writer.toByteArray());

        names.add(node.name);
      }

    } catch (FileNotFoundException e) {
      log.error("No JAR file could be found to write to.", e);
    } catch (IOException e) {
      log.error("IOException thrown while attempting to write class nodes to JAR file. ", e);
    }
  }
}
