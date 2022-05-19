package com.gadarts.necromine.editor.desktop.gui.commands;

import com.gadarts.necromine.editor.desktop.gui.managers.Managers;
import com.gadarts.necromine.editor.desktop.gui.managers.PersistenceManager;

import java.awt.event.ActionEvent;

import static com.gadarts.necromine.editor.desktop.gui.Gui.DEFAULT_MAP_NAME;
import static com.gadarts.necromine.editor.desktop.gui.Gui.PROGRAM_TILE;
import static com.gadarts.necromine.editor.desktop.gui.Gui.SETTINGS_FILE;
import static com.gadarts.necromine.editor.desktop.gui.Gui.SETTINGS_KEY_LAST_OPENED_FILE;
import static com.gadarts.necromine.editor.desktop.gui.Gui.WINDOW_HEADER;
import static org.lwjgl.opengl.Display.setTitle;


public class NewMapCommand extends MapperCommand {

	public NewMapCommand(com.necromine.editor.MapRenderer mapRenderer,
						 Managers managers) {
		super(mapRenderer, managers);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		resetCurrentlyOpenedFile();
		getMapRenderer().onNewMapRequested();
	}

	private void resetCurrentlyOpenedFile( ) {
		PersistenceManager persistenceManager = getManagers().getPersistenceManager();
		persistenceManager.setCurrentlyOpenedMap(null);
		setTitle(String.format(WINDOW_HEADER, PROGRAM_TILE, DEFAULT_MAP_NAME));
		persistenceManager.getSettings().put(SETTINGS_KEY_LAST_OPENED_FILE, null);
		persistenceManager.saveSettings(persistenceManager.getSettings(), SETTINGS_FILE);
	}
}
