package com.gadarts.necromine.editor.desktop.toolbar;

import com.gadarts.necromine.editor.desktop.commands.RotateSelectionCommand;
import com.gadarts.necromine.editor.desktop.commands.SetTilesToolCommand;
import com.necromine.editor.mode.EditModes;
import com.necromine.editor.mode.EditorMode;
import com.necromine.editor.mode.TilesTools;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum SubToolbarsDefinitions {
	EMPTY(new EditModes[]{}),

	MODE_TILES(new EditModes[]{EditModes.TILES},
			new SubToolbarButtonDefinition(
					new ToolbarButtonProperties(
							"brush",
							"Place Tiles",
							new SetTilesToolCommand(TilesTools.BRUSH),
							Constants.TILE_TOOL)),
			new SubToolbarButtonDefinition(
					new ToolbarButtonProperties(
							"staircase",
							"Set Tile Height",
							new SetTilesToolCommand(TilesTools.LIFT),
							Constants.TILE_TOOL)),
			new SubToolbarButtonDefinition(
					new ToolbarButtonProperties(
							"wall",
							"Tile Walls",
							new SetTilesToolCommand(TilesTools.WALL_TILING),
							Constants.TILE_TOOL))),

	ROTATABLE_MODE(new EditModes[]{EditModes.CHARACTERS, EditModes.ENVIRONMENT},
			new SubToolbarButtonDefinition(
					new ToolbarButtonProperties(
							"rotate_clockwise",
							"Rotate Character Clock-Wise",
							new RotateSelectionCommand(RotateSelectionCommand.CLOCKWISE))),
			new SubToolbarButtonDefinition(
					new ToolbarButtonProperties(
							"rotate_counter_clockwise",
							"Rotate Character Counter Clock-Wise",
							new RotateSelectionCommand(RotateSelectionCommand.COUNTER_CLOCKWISE))));

	private final SubToolbarButtonDefinition[] buttons;
	private final EditModes[] modes;

	SubToolbarsDefinitions(final EditModes[] modes, final SubToolbarButtonDefinition... buttons) {
		this.buttons = buttons;
		this.modes = modes;
	}

	public static SubToolbarsDefinitions findByMode(final EditorMode mode) {
		return Arrays.stream(SubToolbarsDefinitions.values())
				.filter(def -> Arrays.stream(def.getModes()).anyMatch(defMode -> defMode == mode))
				.findFirst()
				.orElse(null);
	}

	private static class Constants {
		public static final String TILE_TOOL = "tile_tool";
	}
}
