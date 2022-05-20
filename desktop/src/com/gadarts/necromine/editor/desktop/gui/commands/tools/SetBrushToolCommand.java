package com.gadarts.necromine.editor.desktop.gui.commands.tools;

import com.gadarts.necromine.editor.desktop.ModesManager;
import com.gadarts.necromine.editor.desktop.gui.managers.Managers;
import com.necromine.editor.MapRenderer;
import com.necromine.editor.mode.EditModes;
import com.necromine.editor.mode.EditorMode;
import com.necromine.editor.mode.tools.EditorTool;
import com.necromine.editor.mode.tools.ElementTools;
import com.necromine.editor.mode.tools.TilesTools;

public class SetBrushToolCommand extends SetToolCommand {

	public SetBrushToolCommand(MapRenderer mapRenderer,
							   Managers managers) {
		super(mapRenderer, managers);
	}

	@Override
	protected EditorTool getTool( ) {
		EditorMode selectedMode = ModesManager.getSelectedMode();
		EditorTool result = null;
		if (selectedMode == EditModes.TILES) {
			result = TilesTools.BRUSH;
		} else if (selectedMode == EditModes.ENVIRONMENT) {
			result = ElementTools.BRUSH;
		}
		return result;
	}

}
