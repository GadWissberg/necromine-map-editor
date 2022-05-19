package com.gadarts.necromine.editor.desktop.gui.toolbar.sub;

import com.gadarts.necromine.editor.desktop.gui.commands.editing.RotateSelectionClockwiseCommand;
import com.gadarts.necromine.editor.desktop.gui.commands.editing.RotateSelectionCounterClockwiseCommand;
import com.gadarts.necromine.editor.desktop.gui.commands.tools.SetLiftToolCommand;
import com.gadarts.necromine.editor.desktop.gui.commands.tools.SetTilesBrushToolCommand;
import com.gadarts.necromine.editor.desktop.gui.commands.tools.SetWallTilingToolCommand;
import com.gadarts.necromine.editor.desktop.gui.toolbar.ToolbarButtonProperties;
import com.necromine.editor.mode.EditModes;
import com.necromine.editor.mode.EditorMode;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum SubToolbarsDefinitions {
	EMPTY(null),

	TILES(EditModes.TILES,
			new SubToolbarButtonDefinition(
					new ToolbarButtonProperties(
							"brush",
							"Place Tiles",
							SetTilesBrushToolCommand.class,
							Constants.BUTTON_GROUP_TOOL)),
			new SubToolbarButtonDefinition(
					new ToolbarButtonProperties(
							"staircase",
							"Set Tile Height",
							SetLiftToolCommand.class,
							Constants.BUTTON_GROUP_TOOL)),
			new SubToolbarButtonDefinition(
					new ToolbarButtonProperties(
							"wall",
							"Tile Walls",
							SetWallTilingToolCommand.class,
							Constants.BUTTON_GROUP_TOOL))),

	CHARACTERS(EditModes.CHARACTERS,
			new SubToolbarButtonDefinition(
					new ToolbarButtonProperties(
							"rotate_clockwise",
							"Rotate Character Clock-Wise",
							RotateSelectionClockwiseCommand.class)),
			new SubToolbarButtonDefinition(
					new ToolbarButtonProperties(
							"rotate_counter_clockwise",
							"Rotate Character Counter Clock-Wise",
							RotateSelectionCounterClockwiseCommand.class)));

	////	ENVIRONMENT(EditModes.ENVIRONMENT,
////			new SubToolbarButtonDefinition(
////					new ToolbarButtonProperties(
////							"brush",
////							"Place environment objects",
////							new SetToolCommand(EnvTools.BRUSH),
////							Constants.TOOL)),
////			new SubToolbarButtonDefinition(
////					new ToolbarButtonProperties(
////							"define",
////							"Define a placed environment object",
////							new SetToolCommand(EnvTools.DEFINE),
////							Constants.TOOL)),
////			new SubToolbarButtonDefinition(),
////			new SubToolbarButtonDefinition(
////					new ToolbarButtonProperties(
////							"rotate_clockwise",
////							"Rotate Object Clock-Wise",
////							new RotateSelectionCommand(RotateSelectionCommand.CLOCKWISE))),
////			new SubToolbarButtonDefinition(
////					new ToolbarButtonProperties(
////							"rotate_counter_clockwise",
////							"Rotate Object Counter Clock-Wise",
////							new RotateSelectionCommand(RotateSelectionCommand.COUNTER_CLOCKWISE))));
	private final SubToolbarButtonDefinition[] buttons;
	private final EditModes mode;

	SubToolbarsDefinitions(final EditModes mode, final SubToolbarButtonDefinition... buttons) {
		this.buttons = buttons;
		this.mode = mode;
	}

	public static SubToolbarsDefinitions findByMode(final EditorMode mode) {
		return Arrays.stream(SubToolbarsDefinitions.values())
				.filter(def -> def.mode == mode)
				.findFirst()
				.orElse(null);
	}

	private static class Constants {
		public static final String BUTTON_GROUP_TOOL = "tool";
	}
}
