import com.spark.applet.AppletCreator;
import com.spark.applet.GameStub;
import com.spark.applet.StandardAppletCreator;
import com.spark.io.ArchiveConfigurationReader;
import com.spark.io.ConfigurationReader;
import com.spark.io.InstructionReader;
import com.spark.io.applet.AppletLoader;
import com.spark.io.applet.InjectionAppletLoader;
import com.spark.util.GameType;
import com.spark.util.GamepackQuery;
import com.spark.util.Injector;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Loader
 *
 * @author Ian Caffey
 * @since 1.0
 */
public class Loader {
    public static void main(String[] args) throws Exception {
        ConfigurationReader reader = new ArchiveConfigurationReader();
        AppletLoader loader = new InjectionAppletLoader(new TestInjector());
        AppletCreator creator = new StandardAppletCreator();
        GamepackQuery query = new GamepackQuery(GameType.RS3, 2);
        Map<String, String> configuration = reader.readConfiguration(query);
        JFrame frame = new JFrame(configuration.get(GameStub.WINDOW_TITLE));
        frame.setContentPane(creator.create(loader.load(query, configuration), configuration));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static class TestInjector implements Injector {

        @Override
        public void modify(ClassNode[] nodes) throws IOException {
            for (ClassNode node : nodes) {
                for (MethodNode method : (List<MethodNode>) node.methods) {
                    InstructionReader reader = new InstructionReader(method.instructions);
                    StringBuilder builder = new StringBuilder(method.name + " :: ");
                    reader.read(builder, " ");
                    System.out.println(builder);
                }
            }
        }
    }
}