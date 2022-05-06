package com.gadarts.necromine.editor.desktop.gui.commands;

import com.gadarts.necromine.editor.desktop.ModesManager;
import com.gadarts.necromine.editor.desktop.dialogs.SetAmbientLightDialog;
import com.gadarts.necromine.editor.desktop.gui.DialogsManager;
import com.gadarts.necromine.editor.desktop.gui.PersistenceManager;
import com.necromine.editor.MapRenderer;

import java.awt.event.ActionEvent;

public class OpenAmbientLightDialogCommand extends MapperCommand {


	public OpenAmbientLightDialogCommand(PersistenceManager persistenceManager,
										 com.necromine.editor.MapRenderer mapRenderer,
										 ModesManager modesManager,
										 DialogsManager dialogsManager) {
		super(persistenceManager, mapRenderer, modesManager, dialogsManager);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		MapRenderer mapRenderer = getMapRenderer();
		getDialogsManager().openDialog(new SetAmbientLightDialog(mapRenderer.getAmbientLightValue(), mapRenderer));
	}

}
