package com.spark.adapter;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Adds an interface to a given class via a ClassVisitor (ClassWriter).
 * @Author Christian Bartram
 */
public class AddInterfaceAdapter extends ClassVisitor implements Opcodes {
    private final String[] interfacesToAdd;

    public AddInterfaceAdapter(final ClassVisitor cv, final String... interfacesToAdd){
        super(ASM4, cv);
        this.interfacesToAdd = interfacesToAdd;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces){
        ArrayList<String> interfaceList = new ArrayList<>();

        Collections.addAll(interfaceList, interfacesToAdd);

        cv.visit(version, access, name, signature, superName, interfaceList.toArray(new String[interfaceList.size()]));

    }
}
