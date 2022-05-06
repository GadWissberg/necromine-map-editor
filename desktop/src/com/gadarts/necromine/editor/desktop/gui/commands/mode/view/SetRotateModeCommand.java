package com.gadarts.necromine.editor.desktop.gui.commands.mode.view;

import com.gadarts.necromine.editor.desktop.ModesManager;
import com.gadarts.necromine.editor.desktop.gui.DialogsManager;
import com.gadarts.necromine.editor.desktop.gui.PersistenceManager;
import com.gadarts.necromine.editor.desktop.gui.commands.mode.SetModeCommand;
import com.necromine.editor.mode.EditorMode;
import com.necromine.editor.mode.ViewModes;

public class SetRotateModeCommand extends SetModeCommand {

	public SetRotateModeCommand(PersistenceManager persistenceManager,
								com.necromine.editor.MapRenderer mapRenderer,
								ModesManager modesManager,
								DialogsManager dialogsManager) {
		super(persistenceManager, mapRenderer, modesManager, dialogsManager);
	}

	@Override
	protected EditorMode getMode( ) {
		return ViewModes.ROTATE;
	}


}
