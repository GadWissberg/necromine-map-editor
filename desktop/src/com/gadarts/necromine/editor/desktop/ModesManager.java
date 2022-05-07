package com.gadarts.necromine.editor.desktop;

import com.necromine.editor.mode.EditModes;
import com.necromine.editor.mode.EditorMode;
import com.necromine.editor.mode.tools.EditorTool;
import com.necromine.editor.mode.tools.TilesTools;
import lombok.Getter;

@Getter
public class ModesManager {

	@Getter
	private static EditorMode selectedMode = EditModes.TILES;

	@Getter
	private static EditorTool selectedTool = TilesTools.BRUSH;

	public void applyMode(final EditorMode mode) {
		if (ModesManager.selectedMode == mode) return;
		ModesManager.selectedMode = mode;
	}

	public void applyTool(final EditorTool tool) {
		if (ModesManager.selectedTool == tool) return;
		ModesManager.selectedTool = tool;
	}

}
