package com.spark;

import javax.annotation.PostConstruct;
import javax.swing.*;

import com.spark.util.AppletFactory;
import com.spark.util.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Application - Contains the main class and launches the JFrame with the loaded Applet!
 *
 * @author Cbartram
 * @since 1.0
 */
@SpringBootApplication
public class Application {

	@Autowired
	private Configuration configuration;

	@Autowired
	private AppletFactory launcher;

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
		JFrame frame = new JFrame(configuration.get(Configuration.WINDOW_TITLE));
		frame.setContentPane(launcher.create());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
