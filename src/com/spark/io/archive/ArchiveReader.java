package com.spark.io.archive;

import com.spark.util.Document;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * ArchiveReader
 *
 * @author Ian
 * @version 1.0
 */
public class ArchiveReader implements AutoCloseable {
    public static final ArchiveReader NIL = new ArchiveReader();
    private final InputStream stream;

    private ArchiveReader() {
        this(null);
    }

    protected ArchiveReader(InputStream stream) {
        this.stream = stream;
    }

    public InputStream stream() {
        return stream;
    }

    public int read() throws IOException {
        return stream == null ? -1 : stream.read();
    }

    public int read(byte[] b) throws IOException {
        return stream == null ? -1 : stream.read(b);
    }

    public int read(byte[] b, int off, int len) throws IOException {
        return stream == null ? -1 : stream.read(b, off, len);
    }

    public long skip(long n) throws IOException {
        return stream == null ? -1 : stream.skip(n);
    }

    public int available() throws IOException {
        return stream == null ? -1 : stream.available();
    }

    @Override
    public void close() throws IOException {
        if (stream == null)
            return;
        stream.close();
    }

    public synchronized void mark(int readlimit) {
        if (stream == null)
            return;
        stream.mark(readlimit);
    }

    public synchronized void reset() throws IOException {
        if (stream == null)
            return;
        stream.reset();
    }

    public boolean markSupported() {
        return stream != null && stream.markSupported();
    }

    public Document readDocument() throws IOException {
        return readDocument('\n');
    }

    public Document readDocument(String delimiter) throws IOException {
        return new Document(readLines(delimiter));
    }

    public Document readDocument(char delimiter) throws IOException {
        return new Document(readLines(delimiter));
    }

    public String[] readLines() throws IOException {
        if (stream == null)
            return new String[0];
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null)
            lines.add(line);
        return lines.toArray(new String[lines.size()]);
    }

    public String readLines(char delimiter) throws IOException {
        return readLines(String.valueOf(delimiter));
    }

    public String readLines(String delimiter) throws IOException {
        if (stream == null)
            return null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null)
            builder.append(line).append(delimiter);
        return builder.toString();
    }

    public byte[] readFully() throws IOException {
        if (stream == null)
            return new byte[0];
        byte[] data = new byte[stream.available()];
        if (stream.read(data) == -1)
            return new byte[0];
        return data;
    }

    public Map<String, Class<?>> readMappedClasses() throws IOException {
        if (stream == null)
            return new HashMap<>();
        JarInputStream jis = new JarInputStream(stream);
        Map<String, Class<?>> classes = new HashMap<>();
        JarEntry entry;
        while ((entry = jis.getNextJarEntry()) != null) {
            String name = entry.getName();
            if (!name.endsWith(".class"))
                continue;
            try {
                Class<?> c = Class.forName(name.replace('/', '.').substring(0, name.length() - 6));
                classes.put(c.getName(), c);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return classes;
    }

    public Class<?>[] readClasses() throws IOException {
        if (stream == null)
            return new Class[0];
        JarInputStream jis = new JarInputStream(stream);
        List<Class<?>> classes = new ArrayList<>();
        JarEntry entry;
        while ((entry = jis.getNextJarEntry()) != null) {
            String name = entry.getName();
            if (!name.endsWith(".class"))
                continue;
            try {
                classes.add(Class.forName(name.replace('/', '.').substring(0, name.length() - 6)));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return classes.toArray(new Class[classes.size()]);
    }

    public Map<String, ClassNode> readMappedNodes() throws IOException {
        return readMappedNodes(ClassReader.SKIP_DEBUG);
    }

    public Map<String, ClassNode> readMappedNodes(int flags) throws IOException {
        if (stream == null)
            return new HashMap<>();
        JarInputStream jis = new JarInputStream(stream);
        Map<String, ClassNode> nodes = new HashMap<>();
        JarEntry entry;
        while ((entry = jis.getNextJarEntry()) != null) {
            if (!entry.getName().endsWith(".class"))
                continue;
            byte[] buffer;
            long size = entry.getSize();
            if (size == -1) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                int next;
                while ((next = jis.read()) != -1)
                    stream.write(next);
                buffer = stream.toByteArray();
            } else {
                buffer = new byte[(int) size];
                if (jis.read(buffer) == -1)
                    continue;
            }
            ClassReader reader = new ClassReader(buffer);
            ClassNode node = new ClassNode();
            reader.accept(node, flags);
            nodes.put(node.name, node);
        }
        return nodes;
    }

    public ClassNode[] readNodes() throws IOException {
        return readNodes(ClassReader.SKIP_DEBUG);
    }

    public ClassNode[] readNodes(int flags) throws IOException {
        if (stream == null)
            return new ClassNode[0];
        JarInputStream jis = new JarInputStream(stream);
        List<ClassNode> nodes = new ArrayList<>();
        JarEntry entry;
        while ((entry = jis.getNextJarEntry()) != null) {
            if (!entry.getName().endsWith(".class"))
                continue;
            byte[] buffer;
            long size = entry.getSize();
            if (size == -1) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                int next;
                while ((next = jis.read()) != -1)
                    stream.write(next);
                buffer = stream.toByteArray();
            } else {
                buffer = new byte[(int) size];
                if (jis.read(buffer) == -1)
                    continue;
            }
            ClassReader reader = new ClassReader(buffer);
            ClassNode node = new ClassNode();
            reader.accept(node, flags);
            nodes.add(node);
        }
        return nodes.toArray(new ClassNode[nodes.size()]);
    }
}
