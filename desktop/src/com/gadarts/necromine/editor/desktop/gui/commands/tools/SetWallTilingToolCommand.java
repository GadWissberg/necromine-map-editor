package com.gadarts.necromine.editor.desktop.gui.commands.tools;

import com.gadarts.necromine.editor.desktop.gui.Managers;
import com.necromine.editor.MapRenderer;
import com.necromine.editor.mode.tools.EditorTool;
import com.necromine.editor.mode.tools.TilesTools;

public class SetWallTilingToolCommand extends SetToolCommand {

	public SetWallTilingToolCommand(MapRenderer mapRenderer,
									Managers managers) {
		super(mapRenderer, managers);
	}

	@Override
	protected EditorTool getTool( ) {
		return TilesTools.WALL_TILING;
	}

}
