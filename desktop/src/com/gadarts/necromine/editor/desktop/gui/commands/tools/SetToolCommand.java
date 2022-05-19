package com.gadarts.necromine.editor.desktop.gui.commands.tools;

import com.gadarts.necromine.editor.desktop.ModesManager;
import com.gadarts.necromine.editor.desktop.gui.commands.MapperCommand;
import com.gadarts.necromine.editor.desktop.gui.managers.Managers;
import com.necromine.editor.mode.tools.EditorTool;

import java.awt.event.ActionEvent;

public abstract class SetToolCommand extends MapperCommand {
	private EditorTool tool;

	public SetToolCommand(com.necromine.editor.MapRenderer mapRenderer,
						  Managers managers) {
		super(mapRenderer, managers);
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
