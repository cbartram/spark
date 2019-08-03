package com.spark.transform;

import com.spark.adapter.AddInterfaceAdapter;
import com.spark.adapter.InjectAccessorAdapter;
import com.spark.adapter.MutateSuperClassAdapter;
import com.spark.printer.ClassPrinter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;

/**
 * A set of manipulation utilities. See MainTransform.java on how to use.
 * All methods that have a String parameter must be expressed in type descriptor form.
 *
 * Uses the ASM Core API.
 *
 * @author cbartram
 */

public class AbstractClassTransform implements Opcodes {
    private ClassReader cr = null;
    private ClassWriter cw = null;
    private ClassNode node = null;

    private ClassVisitor changer = null;  // ChangeSuperAdapter
    private ClassVisitor interfaceAdder = null; // AddInterfaceAdapter
    private ClassVisitor adder = null; // AddGetterAdapter

    private ClassVisitor combinedAdapter = null; //Mixture of all adapters

    public AbstractClassTransform(ClassReader cr, ClassWriter cw, ClassNode node) {
        this.cr = cr;
        this.cw = cw;
        this.node = node;

        try {
            cr = new ClassReader(toByteArray(node, 0));
            cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            cr.accept(cw, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

            addGetter("theSecretString",
                    Type.getType(String.class).getDescriptor(),
                    "com/dna/asm/Accessor",
                    "getSecretString",
                    ALOAD,
                    ARETURN
            );
            changeSuperClass("com/dna/asm/accessors/SuperClass");
            finish();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static byte[] toByteArray(ClassNode cnode, int cwFlags) {
        ClassWriter cw = new ClassWriter(cwFlags);
        cnode.accept(cw);
        return cw.toByteArray();
    }


    private void addGetter(final String targetVar, final String descriptor, final String intrFace, final String getterName, final int varInsn, final int retInsn) {
        try {
            interfaceAdder = new AddInterfaceAdapter(combinedAdapter == null ? cw : combinedAdapter, intrFace);
            adder = new InjectAccessorAdapter(interfaceAdder, targetVar, descriptor, getterName, "foo");
            combinedAdapter = adder;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addGetter(final String targetVar, final String descriptor, final String getterName, final int varInsn, final int retInsn) {
        try {
            InjectAccessorAdapter adder = new InjectAccessorAdapter(combinedAdapter == null ? cw : combinedAdapter, targetVar, descriptor, getterName, "foo");  //adds filter to CW
            combinedAdapter = adder;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void changeSuperClass(final String superClass) {
        try {
            changer = new MutateSuperClassAdapter(combinedAdapter == null ? cw : combinedAdapter, superClass);
            combinedAdapter = changer;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void finish() {
        try {
            cr.accept(combinedAdapter == null ? cw : combinedAdapter, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

            byte[] outStream = cw.toByteArray();
            ClassPrinter cp  = new ClassPrinter();
            ClassReader cr = new ClassReader(outStream);
            cr.accept(cp, 0);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}