package com.gadarts.necromine.editor.desktop;

import com.necromine.editor.mode.CameraModes;
import com.necromine.editor.mode.EditModes;
import com.necromine.editor.mode.EditorMode;
import com.necromine.editor.mode.tools.EditorTool;
import com.necromine.editor.mode.tools.EnvTools;
import com.necromine.editor.mode.tools.TilesTools;
import lombok.Getter;

import java.util.Optional;

@Getter
public class ModesHandler {

	@Getter
	private static EditorMode currentMode = EditModes.TILES;

	@Getter
	private static EditorTool tool = TilesTools.BRUSH;

	public void applyMode(final EditorMode mode) {
		if (ModesHandler.currentMode == mode) return;
		Class<? extends EditorMode> modeType = mode.getClass();
		String name = null;
		if (modeType.equals(CameraModes.class)) {
			name = Events.MODE_SET_CAMERA.name();
		} else if (modeType.equals(EditModes.class)) {
			name = Events.MODE_SET_EDIT.name();
		}
		Optional.ofNullable(name).ifPresent(n -> ModesHandler.currentMode = mode);
	}

	public void setTool(final EditorTool tool) {
		if (ModesHandler.tool == tool) return;
		Class<? extends EditorTool> toolType = tool.getClass();
		String name = null;
		if (toolType.equals(TilesTools.class)) {
			name = Events.TILE_TOOL_SET.name();
		} else if (toolType.equals(EnvTools.class)) {
			name = Events.ENV_TOOL_SET.name();
		}
		Optional.ofNullable(name).ifPresent(n -> ModesHandler.tool = tool);
	}

}
