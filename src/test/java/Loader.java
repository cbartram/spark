import com.spark.io.ArchiveConfigurationReader;
import com.spark.lang.RunescapeClassLoader;
import com.spark.printer.ClassPrinter;
import com.spark.util.*;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

/**
 * Loader - Loads the Main Applet and Provides the injection point for Accessor methods to hook
 * into in game values
 *
 * @author Cbartram
 * @since 1.0
 */
public class Loader {

    /**
     * Launches the GUI and loads the Runescape game pack into the Applet
     * @param args String[] CLI args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        //Create the applet and load the classes from it
        AppletLauncher launcher = new AppletLauncher(new ArchiveConfigurationReader(), new InjectionAppletLoader(new ClassInjector()), new StandardAppletCreator());

        //Create Configuration, JFrame and Load the game
        Configuration configuration = launcher.configure(GameType.OLDSCHOOL, 2);
        JFrame frame = new JFrame(configuration.get(Configuration.WINDOW_TITLE));
        frame.setContentPane(launcher.launch(configuration));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     *
     */
    private static class ClassInjector implements Injector {
        HashMap<String, ClassNode> classTree = new HashMap<String, ClassNode>();

        @Override
        public void modify(ClassNode[] nodes) throws IOException {
            ClassPrinter cp = new ClassPrinter();

            for (ClassNode node : nodes) {

                    //Create ClassPrinter, reader and print the original class
                    ClassReader cr = new ClassReader(toByteArray(node));
                    ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);
                    classTree.put(node.name, node);

                    for(FieldNode field: (List<FieldNode>) node.fields) {
                      //  if(field.desc.equalsIgnoreCase("I")) {
                            //Create ClassWriter and a MutateNameAdapter (injects new field into each class)
//                            ClassVisitor injection = new InjectAccessorAdapter(cw, field.name, field.desc, "get" + field.name.toUpperCase(), node.name);
//                            cr.accept(injection, ClassWriter.COMPUTE_FRAMES);
//                            byte[] injectedClass = cw.toByteArray();
//
//                            //Create new Byte Array for Mutated Class and add it to the hashmap
//                            ClassReader cr2 = new ClassReader(injectedClass);
//                            cr2.accept(cp, ClassWriter.COMPUTE_FRAMES);
                        //}
                    }
            }

            System.out.println("[INFO] Class Nodes: " + classTree.toString());

            try {
                Class<?> c = new RunescapeClassLoader(classTree).findClass("jt");
                for(Method m: c.getMethods()) {
//                     System.out.println(m.getName() + " => " + m.invoke(null, null));
                }

            } catch(Exception e) {
                e.printStackTrace();
            }

            System.out.println("Class Name  ->  Method name :: Bytecode instructions");
            //Iterate over the newly created classes (with injected code)
            for(ClassNode node: nodes) {
                for(MethodNode method: (List<MethodNode>) node.methods) {
//                    InstructionReader reader = new InstructionReader(method.instructions);
//                    StringBuilder builder = new StringBuilder(node.name + " -> " + method.name + " :: ");
//                    reader.read(builder, " ");
//                    System.out.println(builder);
                }
            }

        }

        /**
         * Converts a ClassNode to a ByteArray for use in loading the class
         *
         * @param cnode ClassNode Object
         * @return Byte[]
         */
        private static byte[] toByteArray(ClassNode cnode) {
            ClassWriter cw = new ClassWriter(0);
            cnode.accept(cw);
            return cw.toByteArray();
        }


        /**
         * Creates a ClassNode from a byte array to be used for ASM modifications.
         *
         * @param bytes     A byte array representing the class to be modified by ASM
         * @return a new ClassNode instance
         */
        public static ClassNode toClassNode(byte[] bytes) {
            ClassNode cnode = new ClassNode();
            ClassReader reader = new ClassReader(bytes);
            reader.accept(cnode, 0);
            return cnode;
        }

    }
}
