package test;

import com.spark.applet.AppletLoader;
import com.spark.applet.GameStub;
import com.spark.applet.InjectionAppletLoader;
import com.spark.io.ConfigurationReader;
import com.spark.io.StreamConfigurationReader;
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
        ConfigurationReader reader = new StreamConfigurationReader(GameType.RS3, 2);
        AppletLoader loader = new InjectionAppletLoader(reader.getType(), reader.getWorld());
        Map<String, String> configuration = reader.readConfiguration();
        JFrame frame = new JFrame(configuration.get(GameStub.WINDOW_TITLE));
        Applet applet = loader.load(configuration);
        frame.setContentPane(applet);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
