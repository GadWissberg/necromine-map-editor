package com.gadarts.necromine.editor.desktop.gui.commands.mode.edit;

import com.gadarts.necromine.editor.desktop.gui.Managers;
import com.gadarts.necromine.editor.desktop.gui.commands.mode.SetModeCommand;
import com.necromine.editor.mode.EditModes;
import com.necromine.editor.mode.EditorMode;

public class SetLightsModeCommand extends SetModeCommand {

	public SetLightsModeCommand(com.necromine.editor.MapRenderer mapRenderer,
								Managers managers) {
		super(mapRenderer, managers);
	}

	@Override
	protected EditorMode getMode( ) {
		return EditModes.LIGHTS;
	}


}
