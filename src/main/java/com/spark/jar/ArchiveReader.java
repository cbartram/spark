package com.spark.jar;

import com.spark.util.JarUtils;
import com.spark.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.runelite.deob.Deob;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Pack200;
import java.util.zip.GZIPInputStream;

/**
 * ArchiveReader
 * Parses and reads a JAR Archive file into a set of ClassNodes[] which ASM
 * can then use for Bytecode manipulation
 *
 * @author Christian Bartram
 * @since 1.0
 */
@Slf4j
@AllArgsConstructor
@RequiredArgsConstructor
public class ArchiveReader implements AutoCloseable {
    private static int[] p;
    private static char[] m;

    @NonNull
    private final InputStream stream;
    private String obfuscatedJarPath;
    private String deobfuscatedJarPath;
    private boolean saveObfuscatedJar;

    @Override
    public void close() throws IOException {
        if (stream == null)
            return;
        stream.close();
    }

    /**
     * Returns a set of actual Java Classes using reflection. Note: This will NOT return
     * a set of use-able classNodes for ASM injection.
     * @return Class[]
     * @throws IOException
     */
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

    /**
     * Wrapper for reading the class Nodes from the JAR file. This will read the JAR from an input
     * stream opened via a URL connection.
     * @param key
     * @param ivpc
     * @return ClassNode[]
     * @throws Exception
     */
    public ClassNode[] readNodes(String key, String ivpc) throws Exception {
        if (stream == null)
            return new ClassNode[0];
        JarInputStream jis = new JarInputStream(stream);
        List<ClassNode> nodes = new ArrayList<>();
        read(nodes, jis, ClassReader.SKIP_DEBUG, key, ivpc);

        Map<String, ClassNode> map = new HashMap<>();
        for(ClassNode node : nodes) {
            map.put(node.name, node);
        }

        // TODO cache this bc it takes 2-3 minutes to deob a client and only needs to happen 1 time
        // subsequent runs should just directly load the deob jar file
        if(saveObfuscatedJar) {
            final String id = StringUtils.uniqueId();
            final String obfuscatedPath = obfuscatedJarPath.replace("{id}", id);
            final String deobfuscatedPath = deobfuscatedJarPath.replace("{id}", id);
            JarUtils.save(map, obfuscatedPath);
            Deob.deobfuscate(obfuscatedPath, deobfuscatedPath);
            return readNodes(deobfuscatedPath);
        }

        return nodes.toArray(new ClassNode[0]);
    }


    /**
     * This method will read a JAR file from a local filesystem.
     * @param jarFilePath String path to read jar file from
     * @return
     */
    public ClassNode[] readNodes(final String jarFilePath) {
        return JarUtils.read(jarFilePath);
    }

    /**
     * Reads the JAR file into a set of class Nodes recursively
     * @param nodes
     * @param jis
     * @param flags
     * @param key
     * @param ivpc
     * @throws Exception
     */
    public void read(List<ClassNode> nodes, JarInputStream jis, int flags, String key, String ivpc) throws Exception {
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
}
