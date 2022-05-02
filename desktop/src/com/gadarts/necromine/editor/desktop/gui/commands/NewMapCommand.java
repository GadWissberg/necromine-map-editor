package com.gadarts.necromine.editor.desktop.gui.commands;

import com.gadarts.necromine.editor.desktop.gui.FileManager;
import com.gadarts.necromine.editor.desktop.toolbar.MapperCommand;
import com.necromine.editor.GuiEventsSubscriber;

import java.awt.event.ActionEvent;
import java.util.Map;

import static com.gadarts.necromine.editor.desktop.gui.Gui.DEFAULT_MAP_NAME;
import static com.gadarts.necromine.editor.desktop.gui.Gui.PROGRAM_TILE;
import static com.gadarts.necromine.editor.desktop.gui.Gui.SETTINGS_KEY_LAST_OPENED_FILE;
import static com.gadarts.necromine.editor.desktop.gui.Gui.WINDOW_HEADER;
import static org.lwjgl.opengl.Display.setTitle;


public class NewMapCommand extends MapperCommand {

	public NewMapCommand(FileManager fileManager, GuiEventsSubscriber guiEventsSubscriber, Map<String, String> settings) {
		super(fileManager, guiEventsSubscriber, settings);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		resetCurrentlyOpenedFile();
		getGuiEventsSubscriber().onNewMapRequested();
	}

	private void resetCurrentlyOpenedFile() {
		getFileManager().setCurrentlyOpenedMap(null);
		setTitle(String.format(WINDOW_HEADER, PROGRAM_TILE, DEFAULT_MAP_NAME));
		getSettings().put(SETTINGS_KEY_LAST_OPENED_FILE, null);
		getFileManager().saveSettings(getSettings());
	}
}
