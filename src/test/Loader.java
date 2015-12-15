package test;

import com.spark.applet.GameStub;
import com.spark.io.ArchiveConfigurationReader;
import com.spark.io.ConfigurationReader;
import com.spark.io.InstructionReader;
import com.spark.io.applet.AppletLoader;
import com.spark.io.applet.InjectionAppletLoader;
import com.spark.util.GameType;
import com.spark.util.Injector;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import javax.swing.*;
import java.applet.Applet;
import java.io.IOException;
import java.util.Map;

/**
 * Loader
 *
 * @author Ian
 * @version 1.0
 */
public class Loader {
    public static void main(String[] args) throws Exception {
        ConfigurationReader reader = new ArchiveConfigurationReader(GameType.OLDSCHOOL, 2);
        AppletLoader loader = new InjectionAppletLoader(reader.getType(), reader.getWorld(), new TestInjector());
        Map<String, String> configuration = reader.readConfiguration();
        JFrame frame = new JFrame(configuration.get(GameStub.WINDOW_TITLE));
        Applet applet = loader.load(configuration);
        frame.setContentPane(applet);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static class TestInjector implements Injector {

        @Override
        public void modify(ClassNode[] nodes) throws IOException {
            for (ClassNode node : nodes) {
                for (MethodNode method : node.methods) {
                    InstructionReader reader = new InstructionReader(method.instructions);
                    StringBuilder builder = new StringBuilder(method.name + " :: ");
                    reader.read(builder, " ");
                    System.out.println(builder);
                }
            }
        }
    }
}
