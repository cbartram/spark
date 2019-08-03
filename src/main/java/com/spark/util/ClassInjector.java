package com.spark.util;

import com.spark.lang.RunescapeClassLoader;
import com.spark.printer.ClassPrinter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

/**
 * ClassInjector
 * Creates an injector to use ASM to modify bytecode and hook into field values.
 * A set of Runescape class nodes from the JAR file are given as params to the modify
 * method which can be used directly with ASM
 *
 * @author Christian Bartram
 */
public class ClassInjector implements Injector {
	HashMap<String, ClassNode> classTree = new HashMap<String, ClassNode>();

	@Override
	public void modify(ClassNode[] nodes) throws IOException {
		ClassPrinter cp = new ClassPrinter();

		for (ClassNode node : nodes) {

			//Create ClassPrinter, reader and print the original class
			ClassReader cr = new ClassReader(toByteArray(node));
			ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);
			classTree.put(node.name, node);

			if(node.name.equals("ae")) {
				cr.accept(cp, 0);
			}


			for (FieldNode field : (List<FieldNode>) node.fields) {
				//  if(field.desc.equalsIgnoreCase("I")) {
				//Create ClassWriter and a MutateNameAdapter (injects new field into each class)
//                            ClassVisitor injection = new InjectAccessorAdapter(cw, field.name, field.desc, "get" + field.name.toUpperCase(), node.name);
//                            cr.accept(injection, ClassWriter.COMPUTE_FRAMES);
//                            byte[] injectedClass = cw.toByteArray();
//
//                            //Create new Byte Array for Mutated Class and add it to the hashmap
//                            ClassReader cr2 = new ClassReader(injectedClass);
//                            cr2.accept(cp, ClassWriter.COMPUTE_FRAMES);
				//}
			}
		}

		System.out.println("[INFO] Class Nodes: " + classTree.toString());

		try {
			Class<?> c = new RunescapeClassLoader(classTree).findClass("jt");
			for (Method m : c.getMethods()) {}
		} catch (Exception e) {
			e.printStackTrace();
		}

		//Iterate over the newly created classes (with injected code)
		for (ClassNode node : nodes) {
			for (MethodNode method : (List<MethodNode>) node.methods) {
//                    InstructionReader reader = new InstructionReader(method.instructions);
//                    StringBuilder builder = new StringBuilder(node.name + " -> " + method.name + " :: ");
//                    reader.read(builder, " ");
//                    System.out.println(builder);
			}
		}

	}

	/**
	 * Converts a ClassNode to a ByteArray for use in loading the class
	 *
	 * @param cnode ClassNode Object
	 * @return Byte[]
	 */
	private static byte[] toByteArray(ClassNode cnode) {
		ClassWriter cw = new ClassWriter(0);
		cnode.accept(cw);
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