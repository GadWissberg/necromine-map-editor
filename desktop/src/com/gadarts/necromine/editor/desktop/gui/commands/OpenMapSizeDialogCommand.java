package com.gadarts.necromine.editor.desktop.gui.commands;

import com.gadarts.necromine.editor.desktop.dialogs.SetMapSizeDialog;
import com.gadarts.necromine.editor.desktop.gui.managers.Managers;
import com.necromine.editor.MapRenderer;

import java.awt.event.ActionEvent;

public class OpenMapSizeDialogCommand extends MapperCommand {


	public OpenMapSizeDialogCommand(com.necromine.editor.MapRenderer mapRenderer,
									Managers managers) {
		super(mapRenderer, managers);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		MapRenderer mapRenderer = getMapRenderer();
		getManagers().getDialogsManager().openDialog(new SetMapSizeDialog(mapRenderer.getMapSize(), mapRenderer));
	}

}
