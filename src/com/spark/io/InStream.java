package com.spark.io;

import com.spark.util.Document;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * InStream
 *
 * @author Ian
 * @version 1.0
 */
public class InStream implements AutoCloseable {
    public static final InStream NIL = new InStream();
    private final InputStream stream;

    private InStream() {
        this(null);
    }

    protected InStream(InputStream stream) {
        this.stream = stream;
    }

    public InputStream stream() {
        return stream;
    }

    public Document readDocument() {
        return readDocument('\n');
    }

    public Document readDocument(String delimiter) {
        return new Document(readLines(delimiter));
    }

    public Document readDocument(char delimiter) {
        return new Document(readLines(delimiter));
    }

    public String[] readLines() {
        if (stream == null)
            return new String[0];
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        List<String> lines = new ArrayList<>();
        String line;
        try {
            while ((line = reader.readLine()) != null)
                lines.add(line);
        } catch (IOException e) {
            e.printStackTrace();
            return new String[0];
        }
        return lines.toArray(new String[lines.size()]);
    }

    public String readLines(char delimiter) {
        if (stream == null)
            return null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null)
                builder.append(line).append(delimiter);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return builder.toString();
    }

    public String readLines(String delimiter) {
        if (stream == null)
            return null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null)
                builder.append(line).append(delimiter);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return builder.toString();
    }

    public byte[] readFully() {
        if (stream == null)
            return new byte[0];
        try {
            byte[] data = new byte[stream.available()];
            if (stream.read(data) == -1)
                return new byte[0];
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
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

    public Map<String, Class<?>> readMappedClasses() {
        if (stream == null)
            return new HashMap<>();
        try {
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
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public Class<?>[] readClasses() {
        if (stream == null)
            return new Class[0];
        try {
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
        } catch (IOException e) {
            e.printStackTrace();
            return new Class[0];
        }
    }

    public Map<String, ClassNode> readMappedNodes() {
        return readMappedNodes(ClassReader.SKIP_DEBUG);
    }

    public Map<String, ClassNode> readMappedNodes(int flags) {
        if (stream == null)
            return new HashMap<>();
        try {
            JarInputStream jis = new JarInputStream(stream);
            Map<String, ClassNode> nodes = new HashMap<>();
            JarEntry entry;
            while ((entry = jis.getNextJarEntry()) != null) {
                if (!entry.getName().endsWith(".class"))
                    continue;
                try {
                    byte[] buffer = new byte[(int) entry.getSize()];
                    if (jis.read(buffer) == -1)
                        continue;
                    ClassReader reader = new ClassReader(buffer);
                    ClassNode node = new ClassNode();
                    reader.accept(node, flags);
                    nodes.put(node.name, node);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return nodes;
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public ClassNode[] readNodes() {
        return readNodes(ClassReader.SKIP_DEBUG);
    }

    public ClassNode[] readNodes(int flags) {
        if (stream == null)
            return new ClassNode[0];
        try {
            JarInputStream jis = new JarInputStream(stream);
            List<ClassNode> nodes = new ArrayList<>();
            JarEntry entry;
            while ((entry = jis.getNextJarEntry()) != null) {
                if (!entry.getName().endsWith(".class"))
                    continue;
                try {
                    byte[] buffer = new byte[(int) entry.getSize()];
                    if (jis.read(buffer) == -1)
                        continue;
                    ClassReader reader = new ClassReader(buffer);
                    ClassNode node = new ClassNode();
                    reader.accept(node, flags);
                    nodes.add(node);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return nodes.toArray(new ClassNode[nodes.size()]);
        } catch (IOException e) {
            e.printStackTrace();
            return new ClassNode[0];
        }
    }

    @Override
    public void close() {
        if (stream == null)
            return;
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}
