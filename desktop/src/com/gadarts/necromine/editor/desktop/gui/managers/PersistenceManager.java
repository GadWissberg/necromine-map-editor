package com.gadarts.necromine.editor.desktop.gui.managers;

import com.google.gson.Gson;
import com.necromine.editor.MapRenderer;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import static com.gadarts.necromine.editor.desktop.gui.Gui.PROGRAM_TILE;
import static com.gadarts.necromine.editor.desktop.gui.Gui.SETTINGS_FILE;
import static com.gadarts.necromine.editor.desktop.gui.Gui.SETTINGS_KEY_LAST_OPENED_FILE;
import static com.gadarts.necromine.editor.desktop.gui.Gui.WINDOW_HEADER;
import static org.lwjgl.opengl.Display.setTitle;

@Getter
@Setter
public class PersistenceManager extends BaseManager {
	private File currentlyOpenedMap;
	private Gson gson = new Gson();
	private Map<String, String> settings = new HashMap<>();

	public PersistenceManager(MapRenderer mapRenderer) {
		super(mapRenderer);
	}

	private void tryOpeningFile(final File file, MapRenderer mapRenderer, JFrame window) {
		try {
			mapRenderer.onLoadMapRequested(file.getPath());
			updateCurrentlyOpenedFile(file, SETTINGS_FILE);
		} catch (final IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(window, "Failed to open map file!");
		}
	}

	public void readSettingsFile(JFrame window, MapRenderer mapRenderer) {
		File settingsFile = new File(SETTINGS_FILE);
		if (settingsFile.exists()) {
			try {
				Reader json = new FileReader(SETTINGS_FILE);
				//noinspection unchecked
				settings = gson.fromJson(json, HashMap.class);
			} catch (final FileNotFoundException e) {
				e.printStackTrace();
				final String MESSAGE = "Failed to read the editor's settings!";
				JOptionPane.showMessageDialog(window, MESSAGE, PROGRAM_TILE, JOptionPane.ERROR_MESSAGE);
			}
			if (settings.containsKey(SETTINGS_KEY_LAST_OPENED_FILE)) {
				File file = new File(settings.get(SETTINGS_KEY_LAST_OPENED_FILE));
				tryOpeningFile(file, mapRenderer, window);
			}
		} else {
			settings.clear();
		}
	}

	public void updateCurrentlyOpenedFile(final File file, String settingsFilePath) {
		setCurrentlyOpenedMap(file);
		setTitle(String.format(WINDOW_HEADER, PROGRAM_TILE, file.getName()));
		settings.put(SETTINGS_KEY_LAST_OPENED_FILE, file.getPath());
		saveSettings(settings, settingsFilePath);
	}

	public void saveSettings(Map<String, String> settings, String settingsFile) {
		String serialized = gson.toJson(settings);
		try (PrintWriter out = new PrintWriter(settingsFile)) {
			out.println(serialized);
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
