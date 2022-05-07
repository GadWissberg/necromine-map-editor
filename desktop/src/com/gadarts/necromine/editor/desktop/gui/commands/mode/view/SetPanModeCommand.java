package com.gadarts.necromine.editor.desktop.gui.commands.mode.view;

import com.gadarts.necromine.editor.desktop.gui.Managers;
import com.gadarts.necromine.editor.desktop.gui.commands.mode.SetModeCommand;
import com.necromine.editor.mode.EditorMode;
import com.necromine.editor.mode.ViewModes;

public class SetPanModeCommand extends SetModeCommand {

	public SetPanModeCommand(com.necromine.editor.MapRenderer mapRenderer,
							 Managers managers) {
		super(mapRenderer, managers);
	}

	@Override
	protected EditorMode getMode( ) {
		return ViewModes.PAN;
	}


}
