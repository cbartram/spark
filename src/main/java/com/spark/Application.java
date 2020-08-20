package com.spark;

import javax.annotation.PostConstruct;
import javax.swing.*;

import com.spark.io.ArchiveConfigurationReader;
import com.spark.util.AppletLauncher;
import com.spark.util.ClassInjector;
import com.spark.util.Configuration;
import com.spark.util.GameType;
import com.spark.util.InjectionAppletLoader;
import com.spark.util.StandardAppletCreator;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Application - Loads the Main Applet and Provides the injection point for Accessor methods to hook
 * into in game values
 *
 * @author Cbartram
 * @since 1.0
 */
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(Application.class);
		builder.headless(false);
		builder.run(args);
	}

	/**
	 * Launches the GUI and loads the RuneScape game pack into the Applet
	 */
	@PostConstruct
	public void postConstruct() throws Exception {
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
