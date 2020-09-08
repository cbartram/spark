package com.spark.asm;

import com.spark.asm.transformer.AbstractClassTransformer;
import com.spark.asm.transformer.AppletClassTransformer;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * ClassInjector
 * Creates an injector to use ASM to modify bytecode and hook into field values.
 * A set of Runescape class nodes from the JAR file are given as params to the modify
 * method which can be used directly with ASM
 *
 * @author Christian Bartram
 */
@Slf4j
@Component
@NoArgsConstructor
public class ClassInjector implements Injector {

	@Value("${jar.classfiles.save}")
	private boolean writeClassFilesToDisk;

	HashMap<String, ClassNode> classTree = new HashMap<>();

	public ClassNode readClassFromBytes(byte[] bytes) {
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, 0);
		return classNode;
	}

	@Override
	public void modify(ClassNode[] nodes) {
//		ClassPrinter cp = new ClassPrinter();
		AbstractClassTransformer transformer = new AppletClassTransformer();

		for (ClassNode node : nodes) {
			classTree.put(node.name, node);

			if(node.name.equals("class48")) {
				log.info("Modifying RSApplet class bytecode...");
				transformer.transform(node);
//				Uncomment if you want to print a rudimentary version of the modified class.
//				ClassReader cr = new ClassReader(toByteArray(node));
//				cr.accept(cp, 0);
			}

			if(writeClassFilesToDisk) toFile(toByteArray(node), node.name);
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
			if(className.contains("/")) className = className.substring(className.lastIndexOf('/') + 1);
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
