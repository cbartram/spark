package test;

import com.spark.applet.AppletLoader;
import com.spark.applet.GameStub;
import com.spark.applet.InjectionAppletLoader;
import com.spark.io.GamepackReader;
import com.spark.io.StreamGamepackReader;
import com.spark.util.GameType;

import javax.swing.*;
import java.applet.Applet;
import java.util.Map;

/**
 * Loader
 *
 * @author Ian
 * @version 1.0
 */
public class Loader {
    public static void main(String[] args) throws Exception {
        GamepackReader reader = new StreamGamepackReader(GameType.OLDSCHOOL, 1);
        AppletLoader loader = new InjectionAppletLoader(reader.getType(), reader.getWorld());
        Map<String, String> parameters = reader.readParameters();
        JFrame frame = new JFrame(parameters.get(GameStub.WINDOW_TITLE));
        Applet applet = loader.load(parameters);
        frame.setContentPane(applet);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
