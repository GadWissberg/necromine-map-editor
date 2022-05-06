package com.gadarts.necromine.editor.desktop.gui.commands;

import com.gadarts.necromine.editor.desktop.ModesManager;
import com.gadarts.necromine.editor.desktop.dialogs.SetMapSizeDialog;
import com.gadarts.necromine.editor.desktop.gui.DialogsManager;
import com.gadarts.necromine.editor.desktop.gui.PersistenceManager;
import com.necromine.editor.MapRenderer;

import java.awt.event.ActionEvent;

public class OpenMapSizeDialogCommand extends MapperCommand {


	public OpenMapSizeDialogCommand(PersistenceManager persistenceManager,
									com.necromine.editor.MapRenderer mapRenderer,
									ModesManager modesManager,
									DialogsManager dialogsManager) {
		super(persistenceManager, mapRenderer, modesManager, dialogsManager);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		MapRenderer mapRenderer = getMapRenderer();
		getDialogsManager().openDialog(new SetMapSizeDialog(mapRenderer.getMapSize(), mapRenderer));
	}

}
