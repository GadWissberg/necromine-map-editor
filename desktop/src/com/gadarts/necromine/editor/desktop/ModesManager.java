package com.gadarts.necromine.editor.desktop;

import com.necromine.editor.mode.EditModes;
import com.necromine.editor.mode.EditorMode;
import com.necromine.editor.mode.tools.EditorTool;
import com.necromine.editor.mode.tools.TilesTools;
import lombok.Getter;

@Getter
public class ModesManager {

	@Getter
	private static EditorMode currentMode = EditModes.TILES;

	@Getter
	private static EditorTool tool = TilesTools.BRUSH;

	public void applyMode(final EditorMode mode) {
		if (ModesManager.currentMode == mode) return;
		ModesManager.currentMode = mode;
	}

	public void setTool(final EditorTool tool) {
	}

}
