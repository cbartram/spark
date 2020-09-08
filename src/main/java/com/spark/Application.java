package com.spark;

import com.spark.applet.AppletFactory;
import com.spark.configuration.RunescapeConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;

/**
 * Application - Contains the main class and launches the JFrame with the loaded Applet!
 *
 * @author Cbartram
 * @since 1.0
 */
@Slf4j
@SpringBootApplication
public class Application {

	@Autowired
	private RunescapeConfiguration configuration;

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
		log.info("Starting Spark...");
		JFrame frame = new JFrame(configuration.get(RunescapeConfiguration.WINDOW_TITLE));
		frame.setContentPane(launcher.create());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setMinimumSize(new Dimension(100, 100));
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
