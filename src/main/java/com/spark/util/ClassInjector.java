package com.spark.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import com.spark.printer.ClassPrinter;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * ClassInjector
 * Creates an injector to use ASM to modify bytecode and hook into field values.
 * A set of Runescape class nodes from the JAR file are given as params to the modify
 * method which can be used directly with ASM
 *
 * @author Christian Bartram
 */
@Slf4j
@NoArgsConstructor
public class ClassInjector implements Injector {
	HashMap<String, ClassNode> classTree = new HashMap<String, ClassNode>();

	public ClassNode readClassFromBytes(byte[] bytes) {
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, 0);
		return classNode;
	}

	@Override
	public void modify(ClassNode[] nodes) {
		ClassPrinter cp = new ClassPrinter();

		for (ClassNode node : nodes) {

			//Create ClassPrinter, reader and print the original class
			ClassReader cr = new ClassReader(toByteArray(node));
			ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);
			classTree.put(node.name, node);

//			ClassReader cr2 = new ClassReader(toByteArray(node));
//			cr2.accept(cp, ClassWriter.COMPUTE_FRAMES);

			if(node.name.equals("e")) {
				// Prints the class
				cr.accept(cp, 0);
				for (FieldNode field : (List<FieldNode>) node.fields) {
					if(field.desc.equalsIgnoreCase("I")) {
						// Create ClassWriter and a MutateNameAdapter (injects new field into each class)
						System.out.println("MA Field: " + field.name + " " + field.desc + " " + field.signature);
//						ClassVisitor injection = new InjectAccessorAdapter(cw, field.name, field.desc, "get" + field.name.toUpperCase(), node.name);
//						cr.accept(injection, ClassWriter.COMPUTE_FRAMES);
//						byte[] injectedClass = cw.toByteArray();
//						//Create new Byte Array for Mutated Class and add it to the hashmap
//						ClassReader cr2 = new ClassReader(injectedClass);
//						cr2.accept(cp, ClassWriter.COMPUTE_FRAMES);
//						classTree.remove(node.name);
//						classTree.put(node.name, readClassFromBytes(injectedClass));
					}
				}
			}
		}
	}

	/**
	 * Writes an array of bytes to a class file given the file name and an
	 * array of bytes
	 * @param bytes Byte[] array of bytes to write
	 * @param className String the className of the file to write to
	 */
	private static void toFile(byte[] bytes, String className) {
		try {
			OutputStream os = new FileOutputStream(new File("/Users/christianbartram/IdeaProjects/spark/classes/" + className + ".class"));
			os.write(bytes);
			os.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Converts a ClassNode to a ByteArray for use in loading the class
	 *
	 * @param classNode ClassNode Object
	 * @return Byte[]
	 */
	private static byte[] toByteArray(ClassNode classNode) {
		ClassWriter cw = new ClassWriter(0);
		classNode.accept(cw);
		return cw.toByteArray();
	}


	/**
	 * Creates a ClassNode from a byte array to be used for ASM modifications.
	 *
	 * @param bytes     A byte array representing the class to be modified by ASM
	 * @return a new ClassNode instance
	 */
	public static ClassNode toClassNode(byte[] bytes) {
		ClassNode cnode = new ClassNode();
		ClassReader reader = new ClassReader(bytes);
		reader.accept(cnode, 0);
		return cnode;
	}
}
