import com.iancaffey.spark.applet.GameStub;
import com.iancaffey.spark.io.ArchiveConfigurationReader;
import com.iancaffey.spark.io.InstructionReader;
import com.iancaffey.spark.util.*;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

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
        GamepackQuery query = new GamepackQuery(GameType.OLDSCHOOL, 2);
        Configuration configuration = reader.configure(query);
        JFrame frame = new JFrame(configuration.get(Configuration.WINDOW_TITLE));
        frame.setContentPane(creator.create(loader.load(configuration), configuration));
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
