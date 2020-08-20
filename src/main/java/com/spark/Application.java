package com.spark;

import com.spark.io.ArchiveConfigurationReader;
import com.spark.util.*;

import javax.swing.*;

/**
 * com.spark.Application - Loads the Main Applet and Provides the injection point for Accessor methods to hook
 * into in game values
 *
 * @author Cbartram
 * @since 1.0
 */
public class Application {

	/**
	 * Launches the GUI and loads the Runescape game pack into the Applet
	 * @param args String[] CLI args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		//Create the applet and load the classes from it
		AppletLauncher launcher = new AppletLauncher(
				new ArchiveConfigurationReader(),
				new InjectionAppletLoader(new ClassInjector()),
				new StandardAppletCreator()
		);

		//Create Configuration, JFrame and Load the game
		Configuration configuration = launcher.configure(GameType.OLDSCHOOL, 2);
		JFrame frame = new JFrame(configuration.get(Configuration.WINDOW_TITLE));
		frame.setContentPane(launcher.launch(configuration));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
