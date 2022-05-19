package com.gadarts.necromine.editor.desktop.gui.commands.tools;

import com.gadarts.necromine.editor.desktop.gui.managers.Managers;
import com.necromine.editor.MapRenderer;
import com.necromine.editor.mode.tools.EditorTool;
import com.necromine.editor.mode.tools.TilesTools;

public class SetLiftToolCommand extends SetToolCommand {

	public SetLiftToolCommand(MapRenderer mapRenderer,
							  Managers managers) {
		super(mapRenderer, managers);
	}

	@Override
	protected EditorTool getTool( ) {
		return TilesTools.LIFT;
	}


}
