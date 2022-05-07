package com.gadarts.necromine.editor.desktop.gui.commands.mode;

import com.gadarts.necromine.editor.desktop.ModesManager;
import com.gadarts.necromine.editor.desktop.gui.Managers;
import com.gadarts.necromine.editor.desktop.gui.commands.MapperCommand;
import com.necromine.editor.mode.EditModes;
import com.necromine.editor.mode.EditorMode;
import com.necromine.editor.mode.ViewModes;

import java.awt.event.ActionEvent;

public abstract class SetModeCommand extends MapperCommand {
	public SetModeCommand(com.necromine.editor.MapRenderer mapRenderer,
						  Managers managers) {
		super(mapRenderer, managers);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		applyMode();
	}

	private void applyMode( ) {
		if (ModesManager.getSelectedMode() == getMode()) return;
		EditorMode mode = getMode();
		getManagers().getModesManager().applyMode(mode);
		getManagers().getToolbarsManager().updateSubToolbar(mode);
		if (mode instanceof EditModes) {
			getMapRenderer().onEditModeSet((EditModes) mode);
		} else if (mode instanceof ViewModes) {
			getMapRenderer().onViewModeSet((ViewModes) mode);
		}
	}

	protected abstract EditorMode getMode( );
}
