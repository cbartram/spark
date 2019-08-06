package com.spark.adapter;


import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureVisitor;

/**
 * Modifies existing bytecode to inject a public accessor
 * method into the provided field name. This will be called using
 * reflection to access the field and get its value at runtime
 * @author Christian Bartram
 */
public class InjectAccessorAdapter extends ClassVisitor implements Opcodes {

    private String fieldName = null;
    private String getterName = null;
    private String fieldType = null;
    private String signature = null;
    private String targetClass = null;

    private boolean isFieldPresent = false,
                    isMethodPresent = false;

    /**
     * Constructor for Transforming a ByteCode Class
     * @param cv ClassVisitor object
     * @param fieldName The name of the field e.g. (int someField = 0; someField is the fieldName)
     * @param fieldType The data type of the field (int someField = 0; int is the fieldType)
     * @param getterName The name of the accessor method being injected
     * @param targetClass The name of the class to inject
     */
    public InjectAccessorAdapter(final ClassVisitor cv, final String fieldName, final String fieldType, final String getterName, final String targetClass) {
        super(ASM4, cv);
        this.fieldName = fieldName;
        this.getterName = getterName;
        this.targetClass = targetClass;
        this.fieldType = fieldType;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        cv.visit(V1_5, ACC_PUBLIC, name, signature, superName, interfaces);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value){
        if(name.equals(fieldName) && desc.equals(fieldType)){
            isFieldPresent = true;
            this.signature = signature;
        }

        return cv.visitField(access, name, desc, signature, value);
    }
    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        if (name.equals(getterName) && desc.equals(fieldType)) {
            isMethodPresent = true;
        }
        return cv.visitMethod(access, name, desc, signature, exceptions);
    }

    @Override
    public void visitEnd() {
        if(isFieldPresent && !isMethodPresent) {
            //Convert Field Type into Fully Quantified Version for JVM
            MethodVisitor mv = cv.visitMethod(ACC_PUBLIC, getterName, "()"+fieldType, signature, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, targetClass, fieldName, fieldType);
            //GETFIELD java/lang/String.name : Ljava/lang/String;
            //OPCODE   ClassName        FieldName  : Field Type (Long)
            int opCode = getReturnOpcode(fieldType);
            mv.visitMaxs(opCode == LRETURN || opCode == DRETURN ? 2 : 1, 0);
            mv.visitInsn(opCode);

            mv.visitEnd();
        } else {
            cv.visitEnd();
        }
    }

    /**
     * Returns the OPCode for the Data type given
     * @param desc
     * @return
     */
    private int getReturnOpcode(String desc) {
        desc = desc.substring(desc.indexOf(")") + 1);
        if (desc.length() > 1) {
            return ARETURN;
        }
        char c = desc.charAt(0);
        switch (c) {
            case 'I':
            case 'Z':
            case 'B':
            case 'S':
            case 'C':
                return IRETURN;
            case 'J':
                return LRETURN;
            case 'F':
                return FRETURN;
            case 'D':
                return DRETURN;
        }
        throw new RuntimeException("eek");
    }

}
