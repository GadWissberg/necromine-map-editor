package com.gadarts.necromine.editor.desktop.gui.commands;

import com.gadarts.necromine.editor.desktop.ModesManager;
import com.gadarts.necromine.editor.desktop.gui.DialogsManager;
import com.gadarts.necromine.editor.desktop.gui.PersistenceManager;

import java.awt.event.ActionEvent;

import static com.gadarts.necromine.editor.desktop.gui.Gui.*;
import static org.lwjgl.opengl.Display.setTitle;


public class NewMapCommand extends MapperCommand {

	public NewMapCommand(PersistenceManager persistenceManager,
						 com.necromine.editor.MapRenderer mapRenderer,
						 ModesManager modesManager,
						 DialogsManager dialogsManager) {
		super(persistenceManager, mapRenderer, modesManager, dialogsManager);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		resetCurrentlyOpenedFile();
		getMapRenderer().onNewMapRequested();
	}

	private void resetCurrentlyOpenedFile( ) {
		PersistenceManager persistenceManager = getPersistenceManager();
		persistenceManager.setCurrentlyOpenedMap(null);
		setTitle(String.format(WINDOW_HEADER, PROGRAM_TILE, DEFAULT_MAP_NAME));
		persistenceManager.getSettings().put(SETTINGS_KEY_LAST_OPENED_FILE, null);
		persistenceManager.saveSettings(persistenceManager.getSettings());
	}
}
