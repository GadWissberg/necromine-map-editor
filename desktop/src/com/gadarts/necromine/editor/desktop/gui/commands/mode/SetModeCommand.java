package com.gadarts.necromine.editor.desktop.gui.commands.mode;

import com.gadarts.necromine.editor.desktop.ModesManager;
import com.gadarts.necromine.editor.desktop.gui.DialogsManager;
import com.gadarts.necromine.editor.desktop.gui.PersistenceManager;
import com.gadarts.necromine.editor.desktop.gui.commands.MapperCommand;
import com.necromine.editor.mode.EditModes;
import com.necromine.editor.mode.EditorMode;
import com.necromine.editor.mode.ViewModes;

import java.awt.event.ActionEvent;

public abstract class SetModeCommand extends MapperCommand {
	public SetModeCommand(PersistenceManager persistenceManager,
						  com.necromine.editor.MapRenderer mapRenderer,
						  ModesManager modesManager,
						  DialogsManager dialogsManager) {
		super(persistenceManager, mapRenderer, modesManager, dialogsManager);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		applyMode();
	}

	private void applyMode( ) {
		if (ModesManager.getCurrentMode() == getMode()) return;
		EditorMode mode = getMode();
		getModesManager().applyMode(mode);
		if (mode instanceof EditModes) {
			getMapRenderer().onEditModeSet((EditModes) mode);
		} else if (mode instanceof ViewModes) {
			getMapRenderer().onViewModeSet((ViewModes) mode);
		}
	}

	protected abstract EditorMode getMode( );
}
