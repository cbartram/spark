package com.spark.asm.adapter;


import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Created by christianbartram on 1/12/18.
 * Changes the superclass of the given class via the ClassVisitor (ClassWriter).
 * http://github.com/cbartram
 */
public class MutateSuperClassAdapter extends ClassVisitor implements Opcodes {

        private final String superClass;

        public MutateSuperClassAdapter(final ClassVisitor cv, final String superClass){
            super(ASM4, cv);
            this.superClass = superClass;
        }

        @Override
        public void visit(int version, int access, java.lang.String name, java.lang.String signature, java.lang.String superName, java.lang.String[] interfaces) {
            cv.visit(version, access, name, signature, superClass, interfaces);
        }
}
