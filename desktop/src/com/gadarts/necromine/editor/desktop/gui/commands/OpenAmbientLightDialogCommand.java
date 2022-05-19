package com.gadarts.necromine.editor.desktop.gui.commands;

import com.gadarts.necromine.editor.desktop.dialogs.SetAmbientLightDialog;
import com.gadarts.necromine.editor.desktop.gui.managers.DialogsManager;
import com.gadarts.necromine.editor.desktop.gui.managers.Managers;
import com.necromine.editor.MapRenderer;

import java.awt.event.ActionEvent;

public class OpenAmbientLightDialogCommand extends MapperCommand {


	public OpenAmbientLightDialogCommand(com.necromine.editor.MapRenderer mapRenderer,
										 Managers managers) {
		super(mapRenderer, managers);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		MapRenderer mapRenderer = getMapRenderer();
		DialogsManager dialogsManager = getManagers().getDialogsManager();
		dialogsManager.openDialog(new SetAmbientLightDialog(mapRenderer.getAmbientLightValue(), mapRenderer));
	}

}
