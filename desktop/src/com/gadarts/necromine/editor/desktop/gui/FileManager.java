package com.gadarts.necromine.editor.desktop.gui;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;

import static com.gadarts.necromine.editor.desktop.gui.Gui.SETTINGS_FILE;

@Getter
@Setter
public class FileManager {
	private File currentlyOpenedMap;
	private Gson gson = new Gson();

	public void saveSettings(Map<String, String> settings) {
		String serialized = gson.toJson(settings);
		try (PrintWriter out = new PrintWriter(SETTINGS_FILE)) {
			out.println(serialized);
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
