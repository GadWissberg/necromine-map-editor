package com.gadarts.necromine.editor.desktop.gui.commands.tools;

import com.gadarts.necromine.editor.desktop.ModesManager;
import com.gadarts.necromine.editor.desktop.gui.DialogsManager;
import com.gadarts.necromine.editor.desktop.gui.PersistenceManager;
import com.gadarts.necromine.editor.desktop.gui.commands.MapperCommand;
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

	protected abstract EditorTool getTool( );

	@Override
	public void actionPerformed(final ActionEvent e) {
		applyTool();
	}

	private void applyTool( ) {
		if (ModesManager.getSelectedTool() == getTool()) return;
		getMapRenderer().onToolSet(getTool());
	}
}
