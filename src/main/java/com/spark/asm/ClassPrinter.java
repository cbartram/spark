package com.spark.asm;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import lombok.extern.slf4j.Slf4j;

import static org.objectweb.asm.Opcodes.ASM4;

/**
 * Created by christianbartram on 1/11/18.
 * This class prints the bytecode class structure for a given stream of bytes by
 * visiting the method declarations and fields within the class. It does not currently
 * print or parse the source, outer class, or annotations
 *
 * @author Christian Bartram
 */
@Slf4j
public class ClassPrinter extends ClassVisitor {
    private String name;

    public ClassPrinter() {
        super(ASM4);
    }

    @Slf4j
    static class ClassAnnotationValueScanner extends AnnotationVisitor {
        private String description;
        private String className;

        ClassAnnotationValueScanner(final String description, final String className) {
            super(ASM4);
            this.description = description;
            this.className = className;
        }

        @Override
        public void visit(String name, Object value) {
            String annotationName = description.substring(description.lastIndexOf('/') + 1, description.length() - 1);
            log.info("@{}({}=\"{}\")", annotationName, name, value);
            if(annotationName.equalsIgnoreCase("ObfuscatedName") && value.toString().equalsIgnoreCase("bd")) {
                log.info("THE CLASS: {} IS ACTUALLY: Player", className);
            }
            super.visit(name, value);
        }
    }

    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        String builder = access + " " + name +
                " extends " +
                superName +
                " implements " +
                String.join(" ,", interfaces);
        System.out.println(builder);
        this.name = name;
    }

    public void visitSource(String source, String debug) {

    }

    public void visitOuterClass(String owner, String name, String desc) {

    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return new ClassAnnotationValueScanner(desc, name);
    }

    public void visitAttribute(Attribute attr) {

    }

    public void visitInnerClass(String name, String outerName, String innerName, int access) {

    }

    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        System.out.println(" " + desc + " " + name);
        return null;
    }

    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        System.out.println( " " + name + desc);
        return null;
    }
    public void visitEnd() {
        System.out.println("}");
    }
}
