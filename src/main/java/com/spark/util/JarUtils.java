package com.spark.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import com.spark.jar.ArchiveReader;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import lombok.extern.slf4j.Slf4j;

/**
 * JarUtils - A Collection of useful utilities for reading and writing from Jar files.
 * @author Christian Bartram
 * @since 1.0
 */
@Slf4j
public class JarUtils {

  /**
   * Reads a JAR file from the local File System into an array of ASM ClassNode objects.
   * @param fileName String file name where the jar resides
   * @return ClassNode[] Array of class nodes in the JAR file.
   */
  public static ClassNode[] read(final String fileName) {
    try {
      JarInputStream jis = new JarInputStream(new FileInputStream(new File(fileName)));
      List<ClassNode> nodes = new ArrayList<>();

      new ArchiveReader(jis)
          .read(nodes, jis, ClassReader.SKIP_DEBUG, "null", "null");

      return nodes.toArray(new ClassNode[0]);
    } catch(FileNotFoundException e) {
      log.error("There was an error reading the file: {}. While attempting to load the JAR file. File could not be found.", fileName, e);
      return new ClassNode[] {};
    } catch (IOException e) {
      log.error("IOException thrown while attempting to read JAR file from local filesystem. File = {}", fileName, e);
      return new ClassNode[] {};
    } catch(Exception e) {
      log.error("Generic exception thrown while attempting to read JAR file: {} from local filesystem.", fileName, e);
      return new ClassNode[] {};
    }
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
