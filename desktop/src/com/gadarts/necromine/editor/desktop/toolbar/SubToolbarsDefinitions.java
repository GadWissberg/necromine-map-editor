package com.gadarts.necromine.editor.desktop.toolbar;

import com.gadarts.necromine.editor.desktop.commands.RotateSelectionCommand;
import com.gadarts.necromine.editor.desktop.commands.SetTilesToolCommand;
import com.necromine.editor.mode.TilesTools;
import lombok.Getter;

@Getter
public enum SubToolbarsDefinitions {

	MODE_TILES(
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
	ROTATABLE_MODE(
			new SubToolbarButtonDefinition(
					new ToolbarButtonProperties(
							"rotate_clockwise",
							"Rotate Character Clock-Wise",
							new RotateSelectionCommand(RotateSelectionCommand.CLOCKWISE))),
			new SubToolbarButtonDefinition(
					new ToolbarButtonProperties(
							"rotate_counter_clockwise",
							"Rotate Character Counter Clock-Wise",
							new RotateSelectionCommand(RotateSelectionCommand.COUNTER_CLOCKWISE)))),
	MODE_LIGHTS(new SubToolbarButtonDefinition(
			new ToolbarButtonProperties(
					"rotate_clockwise",
					"Rotate Character Clock-Wise",
					new RotateSelectionCommand(RotateSelectionCommand.CLOCKWISE))),
			new SubToolbarButtonDefinition(new ToolbarButtonProperties(
					"rotate_counter_clockwise",
					"Rotate Character Counter Clock-Wise",
					new RotateSelectionCommand(RotateSelectionCommand.COUNTER_CLOCKWISE))));

	private final SubToolbarButtonDefinition[] buttons;

	SubToolbarsDefinitions(final SubToolbarButtonDefinition... buttons) {
		this.buttons = buttons;
	}

	private static class Constants {
		public static final String TILE_TOOL = "tile_tool";
	}
}
