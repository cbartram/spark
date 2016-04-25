package com.iancaffey.spark.io;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Pack200;
import java.util.zip.GZIPInputStream;

/**
 * ArchiveReader
 *
 * @author Ian Caffey
 * @since 1.0
 */
public class ArchiveReader implements AutoCloseable {
    public static final ArchiveReader NIL = new ArchiveReader();
    private static int[] p;
    private static char[] m;

    static {
        label0:
        {
            p = new int[128];
            int i1 = 0;
            while (~i1 > ~p.length) {
                p[i1] = -1;
                i1++;
            }
            break label0;

        }
        label1:
        {
            int j1 = 65;
            while (-91 <= ~j1) {
                p[j1] = -65 + j1;
                j1++;
            }
            break label1;
        }
        label2:
        {
            int k1 = 97;
            while (122 >= k1) {
                p[k1] = k1 + -71;
                k1++;
            }
            break label2;
        }
        label3:
        {
            int l1 = 48;
            while (-58 <= ~l1) {
                p[l1] = (-48 + l1) - -52;
                l1++;
            }
            break label3;
        }
        label4:
        {
            p[43] = 62;
            int ai[] = p;
            ai[42] = 62;
            p[47] = 63;
            int ai1[] = p;
            ai1[45] = 63;
            m = new char[64];
            int i2 = 0;
            while (26 > i2) {
                m[i2] = (char) (65 + i2);
                i2++;
            }
            break label4;
        }
        label5:
        {
            int j2 = 26;
            while (~j2 > -53) {
                m[j2] = (char) (-26 + (97 - -j2));
                j2++;
            }
            break label5;
        }
        label6:
        {
            int k2 = 52;
            while (k2 < 62) {
                m[k2] = (char) (-52 + k2 + 48);
                k2++;
            }
            break label6;
        }
        m[63] = '/';
        m[62] = '+';
    }

    static {
        label0:
        {
            p = new int[128];
            int i1 = 0;
            while (~i1 > ~p.length) {
                p[i1] = -1;
                i1++;
            }
            break label0;
        }
        label1:
        {
            int j1 = 65;
            while (-91 <= ~j1) {
                p[j1] = -65 + j1;
                j1++;
            }
            break label1;
        }
        label2:
        {
            int k1 = 97;
            while (122 >= k1) {
                p[k1] = k1 + -71;
                k1++;
            }
            break label2;
        }
        label3:
        {
            int l1 = 48;
            while (-58 <= ~l1) {
                p[l1] = (-48 + l1) - -52;
                l1++;
            }
            break label3;
        }
        label4:
        {
            p[43] = 62;
            int ai[] = p;
            ai[42] = 62;
            p[47] = 63;
            int ai1[] = p;
            ai1[45] = 63;
            m = new char[64];
            int i2 = 0;
            while (26 > i2) {
                m[i2] = (char) (65 + i2);
                i2++;
            }
            break label4;
        }
        label5:
        {
            int j2 = 26;
            while (~j2 > -53) {
                m[j2] = (char) (-26 + (97 - -j2));
                j2++;
            }
            break label5;
        }
        label6:
        {
            int k2 = 52;
            while (k2 < 62) {
                m[k2] = (char) (-52 + k2 + 48);
                k2++;
            }
            break label6;
        }
        m[63] = '/';
        m[62] = '+';
    }

    private final InputStream stream;

    private ArchiveReader() {
        this(null);
    }

    public ArchiveReader(InputStream stream) {
        this.stream = stream;
    }

    private static byte[] decrypt(String name) throws Exception {
        int j1 = 0;
        int i;
        int j;
        int k;
        int l;
        byte[] abyte0;
        char c1;
        int i1;
        char c2;
        try {
            i = name.length();
            if (0 == i)
                return new byte[0];
        } catch (RuntimeException runtimeexception) {
            throw new Exception("");
        }
        label0:
        {
            label1:
            {
                label2:
                {
                    label3:
                    {
                        label4:
                        {
                            j = -4 & i + 3;
                            k = 3 * (j / 4);
                            if (~i >= ~(-2 + j))
                                break label1;
                            c1 = name.charAt(j - 2);
                            if ('\0' <= c1 && c1 < p.length) {
                                l = p[c1];
                                if (j1 == 0)
                                    break label4;
                            }
                            l = -1;
                        }
                        if (~l == 0)
                            break label1;
                        if (i <= j - 1)
                            break label2;
                        c2 = name.charAt(-1 + j);
                        if (~c2 <= -1 && ~p.length < ~c2) {
                            i1 = p[c2];
                            if (j1 == 0)
                                break label3;
                        }
                        i1 = -1;
                    }
                    if (0 != ~i1 && j1 == 0)
                        break label0;
                }
                k--;
                if (j1 == 0)
                    break label0;
            }
            k -= 2;
        }
        abyte0 = new byte[k];
        decrypt2(abyte0, 0, name, (byte) -75);
        return abyte0;
    }

    private static int decrypt2(byte[] arg0, int arg1, String arg2, byte arg3) throws Exception {
        int var17 = 0;
        try {
            int var4 = arg1;
            int var5 = arg2.length();
            int var6 = 0;

            int var10000;
            while (true) {
                if (var5 > var6) {
                    char var8 = arg2.charAt(var6);
                    var10000 = var8;
                    if (var17 != 0) {
                        break;
                    }

                    int var7;
                    label128:
                    {
                        if (var8 >= 0 && ~p.length < ~var8) {
                            var7 = p[var8];
                            if (var17 == 0) {
                                break label128;
                            }
                        }

                        var7 = -1;
                    }

                    int var10;
                    label140:
                    {
                        if (var5 <= 1 + var6) {
                            var10 = -1;
                            if (var17 == 0) {
                                break label140;
                            }
                        }

                        int var11;
                        label117:
                        {
                            char var12 = arg2.charAt(var6 + 1);
                            if (0 <= var12 && var12 < p.length) {
                                var11 = p[var12];
                                if (var17 == 0) {
                                    break label117;
                                }
                            }

                            var11 = -1;
                        }

                        var10 = var11;
                    }

                    int var19;
                    label141:
                    {
                        if (~(2 + var6) <= ~var5) {
                            var19 = -1;
                            if (var17 == 0) {
                                break label141;
                            }
                        }

                        int var13;
                        label106:
                        {
                            char var14 = arg2.charAt(2 + var6);
                            if (0 <= var14 && p.length > var14) {
                                var13 = p[var14];
                                if (var17 == 0) {
                                    break label106;
                                }
                            }

                            var13 = -1;
                        }

                        var19 = var13;
                    }

                    int var20;
                    label142:
                    {
                        if (3 + var6 >= var5) {
                            var20 = -1;
                            if (var17 == 0) {
                                break label142;
                            }
                        }

                        int var15;
                        label95:
                        {
                            char var16 = arg2.charAt(3 + var6);
                            if (0 <= var16 && ~var16 > ~p.length) {
                                var15 = p[var16];
                                if (var17 == 0) {
                                    break label95;
                                }
                            }
                            var15 = -1;
                        }
                        var20 = var15;
                    }
                    arg0[arg1++] = (byte) (var7 << 2 | var10 >>> 4);
                    if (var19 != -1 || var17 != 0) {
                        arg0[arg1++] = (byte) (var19 >>> 2 | 240 & var10 << 4);
                        if (~var20 != 0) {
                            arg0[arg1++] = (byte) (var20 | var19 << 6 & 192);
                            var6 += 4;
                            if (var17 == 0) {
                                continue;
                            }
                        }
                    }
                }
                var10000 = arg3;
                break;
            }
            return var10000 > -45 ? 45 : arg1 + -var4;
        } catch (RuntimeException var18) {
            throw new Exception("");
        }
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

    public ClassNode[] readNodes(String key, String ivpc) throws Exception {
        return readNodes(ClassReader.SKIP_DEBUG, key, ivpc);
    }

    public ClassNode[] readNodes(int flags, String key, String ivpc) throws Exception {
        if (stream == null)
            return new ClassNode[0];
        JarInputStream jis = new JarInputStream(stream);
        List<ClassNode> nodes = new ArrayList<>();
        read(nodes, jis, flags, key, ivpc);
        return nodes.toArray(new ClassNode[nodes.size()]);
    }

    private void read(List<ClassNode> nodes, JarInputStream jis, int flags, String key, String ivpc) throws Exception {
        JarEntry entry;
        while ((entry = jis.getNextJarEntry()) != null) {
            if ("inner.pack.gz".equals(entry.getName())) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                int next;
                while ((next = jis.read()) != -1)
                    stream.write(next);
                byte[] buffer = stream.toByteArray();
                byte[] keyBytes = decrypt(key);
                byte[] ivpcBytes = decrypt(ivpc);
                SecretKeySpec spec = new SecretKeySpec(keyBytes, "AES");
                Cipher cipherObject = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipherObject.init(2, spec, new IvParameterSpec(ivpcBytes));
                GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(cipherObject.doFinal(buffer)));
                Pack200.Unpacker unpacker = Pack200.newUnpacker();
                ByteArrayOutputStream bos = new ByteArrayOutputStream(buffer.length);
                JarOutputStream jos = new JarOutputStream(bos);
                unpacker.unpack(gzip, jos);
                jos.close();
                read(nodes, new JarInputStream(new ByteArrayInputStream(bos.toByteArray())), flags, key, ivpc);
                continue;
            }
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
    }
}
