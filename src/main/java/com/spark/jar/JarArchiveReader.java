package com.spark.jar;

import com.spark.configuration.GameType;
import com.spark.configuration.RunescapeConfiguration;
import com.spark.interfaces.AbstractReader;
import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.tree.ClassNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

@Slf4j
@Component
public class JarArchiveReader extends AbstractReader<ClassNode[]> {

    @Autowired
    private RunescapeConfiguration configuration;

    @Value("${gametype.type}")
    private GameType type;

    @Value("${gametype.world}")
    private int world;

    @Value("${jar.obfuscated.path}")
    private String obfuscatedJarPath;

    @Value("${jar.deobfuscated.path}")
    private String deobfuscatedJarPath;

    @Value("${jar.obfuscated.save}")
    private boolean saveObfuscatedJar;

//    public ArchiveReader createArchiveReader() {
//        final String jarPath = "src/main/resources/jar/deobfuscated";
//        try {
//            File deobFolder = new File(jarPath);
//            if (deobFolder.isDirectory() && Objects.requireNonNull(deobFolder.listFiles()).length > 0) {
//                // Grab the first file
//                final File deobJarFile = Objects.requireNonNull(deobFolder.listFiles())[0];
//                log.info("Loading pre-deobfuscated Jar file from: {}/{}", jarPath, deobJarFile.getName());
//                return new ArchiveReader(new FileInputStream(deobJarFile));
//            } else {
//                log.info("No deobfuscated JAR files exist in dir: {}. Retrieving RS GamePack and will attempt to deobfuscate...", jarPath);
//                return new ArchiveReader(connection.getInputStream(), obfuscatedPath, deobfuscatedPath, saveObfuscatedJar);
//            }
//        } catch(FileNotFoundException e) {
//            log.warn("No file found in deob directory: {} even though directory has more than 1 file. Loading from RS Config...", jarPath, e);
//            return new ArchiveReader(connection.getInputStream(), obfuscatedPath, deobfuscatedPath, saveObfuscatedJar);
//        }
//    }

    @Override
    public ClassNode[] read() {
        // Builds a url to retrieve the game pack from the configuration (gamepack_6388569.jar) for a given world.
        // The key here which makes this different from configuration ArchiveReader is that we are reading the game pack.
        // with #getGamepack() and not a string of key value pairs.
        InputStream stream = null;
        try {
            stream = open(new URL(String.format(
                    type.getGamepack(),
                    world,
                    configuration.get(RunescapeConfiguration.INITIAL_JAR)
            )));
            return new ArchiveReader(stream, obfuscatedJarPath, deobfuscatedJarPath, saveObfuscatedJar)
                    .readNodes(configuration.get("0"), configuration.get("-1"));
        } catch(MalformedURLException e) {
            log.error("There was an error forming a URL from the string: {}.", String.format(
                type.getGamepack(),
                world,
                configuration.get(RunescapeConfiguration.INITIAL_JAR)), e);
            return new ClassNode[] {};
        } catch(Exception e) {
            log.error("There was some exception thrown while attempting to read the RuneScape JAR Archive from URL: {}.", String.format(
                type.getGamepack(),
                world,
                configuration.get(RunescapeConfiguration.INITIAL_JAR)), e);
            return new ClassNode[] {};
        } finally {
            if(stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    log.error("Failed to close IO connection to host: {}", String.format(type.getGamepack(), world, configuration.get(RunescapeConfiguration.INITIAL_JAR)), e);
                }
            } else {
                log.warn("Connection object while opening URL connection was null. Nothing to close.");
            }
        }
    }
}
