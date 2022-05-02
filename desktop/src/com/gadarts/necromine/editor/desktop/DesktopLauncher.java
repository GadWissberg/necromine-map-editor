package com.gadarts.necromine.editor.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gadarts.necromine.editor.desktop.gui.Gui;
import com.necromine.editor.MapEditor;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;
import java.util.Properties;

public class DesktopLauncher {

	public static final String PROPERTIES_KEY_ASSETS_PATH = "assets.path";
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	public static final String PROPERTIES_FILE_NAME = "settings.properties";

	public static void main(final String[] arg) {
		EventQueue.invokeLater(() -> {
			LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
			config.samples = 3;
			config.width = WIDTH;
			config.height = HEIGHT;
			Properties properties = getProperties();
			MapEditor mapManager = new MapEditor(WIDTH, HEIGHT, properties.getProperty(PROPERTIES_KEY_ASSETS_PATH));
			LwjglAWTCanvas lwjgl = new LwjglAWTCanvas(mapManager, config);
			Gui gui = new Gui(lwjgl, mapManager, properties);
			mapManager.subscribeForEvents(gui);
		});
	}

	private static Properties getProperties() {
		Properties result = null;
		try (InputStream input = new FileInputStream(PROPERTIES_FILE_NAME)) {
			Properties prop = new Properties();
			prop.load(input);
			result = prop;
		} catch (final IOException ignored) {
		}

		return Optional.ofNullable(result).orElseGet(() -> {
			Properties props = null;
			try (OutputStream output = new FileOutputStream(PROPERTIES_FILE_NAME)) {
				props = new Properties();
				props.store(output, null);
			} catch (final IOException io) {
				io.printStackTrace();
			}
			return props;
		});
	}

}
