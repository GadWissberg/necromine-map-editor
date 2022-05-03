package com.gadarts.necromine.editor.desktop.commands.mode;

import com.gadarts.necromine.editor.desktop.ModesHandler;
import com.gadarts.necromine.editor.desktop.gui.PersistenceManager;
import com.necromine.editor.GuiEventsSubscriber;
import com.necromine.editor.mode.EditModes;
import com.necromine.editor.mode.EditorMode;

public class SetTilesModeCommand extends SetModeCommand {

	public SetTilesModeCommand(PersistenceManager persistenceManager,
							   GuiEventsSubscriber guiEventsSubscriber,
							   ModesHandler modesHandler) {
		super(persistenceManager, guiEventsSubscriber, modesHandler);
	}

	@Override
	protected EditorMode getMode( ) {
		return EditModes.TILES;
	}


}
