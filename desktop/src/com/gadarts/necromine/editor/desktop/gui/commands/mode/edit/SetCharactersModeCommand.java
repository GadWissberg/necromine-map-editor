package com.gadarts.necromine.editor.desktop.gui.commands.mode.edit;

import com.gadarts.necromine.editor.desktop.ModesManager;
import com.gadarts.necromine.editor.desktop.gui.DialogsManager;
import com.gadarts.necromine.editor.desktop.gui.PersistenceManager;
import com.gadarts.necromine.editor.desktop.gui.commands.mode.SetModeCommand;
import com.necromine.editor.mode.EditModes;
import com.necromine.editor.mode.EditorMode;

public class SetCharactersModeCommand extends SetModeCommand {

	public SetCharactersModeCommand(PersistenceManager persistenceManager,
									com.necromine.editor.MapRenderer mapRenderer,
									ModesManager modesManager,
									DialogsManager dialogsManager) {
		super(persistenceManager, mapRenderer, modesManager, dialogsManager);
	}

	@Override
	protected EditorMode getMode( ) {
		return EditModes.CHARACTERS;
	}


}
