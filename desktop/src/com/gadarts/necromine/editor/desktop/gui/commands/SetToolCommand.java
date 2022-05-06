package com.gadarts.necromine.editor.desktop.gui.commands;

import com.gadarts.necromine.editor.desktop.ModesManager;
import com.gadarts.necromine.editor.desktop.gui.DialogsManager;
import com.gadarts.necromine.editor.desktop.gui.PersistenceManager;
import com.necromine.editor.mode.tools.EditorTool;

import java.awt.event.ActionEvent;

public abstract class SetToolCommand extends MapperCommand {
	private EditorTool tool;

	public SetToolCommand(PersistenceManager persistenceManager,
						  com.necromine.editor.MapRenderer mapRenderer,
						  ModesManager modesManager,
						  DialogsManager dialogsManager) {
		super(persistenceManager, mapRenderer, modesManager, dialogsManager);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
	}
}
