package com.gadarts.necromine.editor.desktop.gui.commands.mode.view;

import com.gadarts.necromine.editor.desktop.gui.commands.mode.SetModeCommand;
import com.gadarts.necromine.editor.desktop.gui.managers.Managers;
import com.necromine.editor.mode.EditorMode;
import com.necromine.editor.mode.ViewModes;

public class SetRotateModeCommand extends SetModeCommand {

	public SetRotateModeCommand(com.necromine.editor.MapRenderer mapRenderer,
								Managers managers) {
		super(mapRenderer, managers);
	}

	@Override
	protected EditorMode getMode( ) {
		return ViewModes.ROTATE;
	}


}
