package com.spark.io;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.util.Printer;

import java.io.IOException;

/**
 * InstructionReader
 *
 * @author Ian Caffey
 * @since 1.0
 */
public class InstructionReader implements AutoCloseable {
    private AbstractInsnNode[] instructions;
    private int index = 0;

    public InstructionReader(InsnList instructions) {
        if (instructions == null)
            throw new IllegalArgumentException();
        this.instructions = instructions.toArray();
    }

    public InstructionReader(AbstractInsnNode[] instructions) {
        if (instructions == null)
            throw new IllegalArgumentException();
        this.instructions = instructions;
    }

    public boolean isOpen() {
        return instructions != null;
    }

    private void checkOpen() throws IOException {
        if (instructions == null)
            throw new IOException();
    }

    /**
     * Gets the Name of an Instruction given its OPCode
     * @return
     * @throws IOException
     */
    public String read() throws IOException {
        checkOpen();
        if (index >= instructions.length)
            return null;
        AbstractInsnNode node = instructions[index++];
        if (node == null)
            return null;
        int opcode = node.getOpcode();
        return opcode == -1 ? null : Printer.OPCODES[opcode];
    }

    public int read(StringBuilder builder, char delimiter) throws IOException {
        return read(builder, String.valueOf(delimiter));
    }

    public int read(StringBuilder builder, String delimiter) throws IOException {
        return read(builder, instructions.length - index, delimiter);
    }

    public int read(StringBuilder builder, int length, char delimiter) throws IOException {
        return read(builder, length, String.valueOf(delimiter));
    }

    /**
     * Reads a Set of ASM Bytecode instructions and appends
     * the data to a StringBuilder Object
     * @param builder
     * @param length
     * @param delimiter
     * @return
     * @throws IOException
     */
    public int read(StringBuilder builder, int length, String delimiter) throws IOException {
        if (builder == null || length < 0)
            throw new IllegalArgumentException();
        checkOpen();
        int available = instructions.length - index;
        if (available < 1)
            return -1;
        int len = Math.min(available, length);
        for (int i = 0; i < len; i++) {
            String name = read();
            if (name == null)
                continue;
            builder.append(name);
            if (i != length - 1 && delimiter != null)
                builder.append(delimiter);
        }
        return len;
    }

    public int read(String[] buffer) throws IOException {
        if (buffer == null)
            throw new IllegalArgumentException();
        checkOpen();
        int available = instructions.length - index;
        if (available < 1)
            return -1;
        int length = Math.min(available, buffer.length);
        for (int i = 0; i < length; i++) {
            String name = read();
            if (name == null)
                continue;
            buffer[i] = name;
        }
        return length;
    }

    @Override
    public void close() throws Exception {
        instructions = null;
    }
}
