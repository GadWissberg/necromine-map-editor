package com.gadarts.necromine.editor.desktop;

import com.gadarts.necromine.editor.desktop.actions.RotateSelectionCommand;
import com.gadarts.necromine.editor.desktop.toolbar.ToolbarButtonOfMenuItem;
import lombok.Getter;

@Getter
public enum SubToolbarsDefinitions {
	MODE_OBJECTS(new SubToolbarButtonDefinition(new ToolbarButtonOfMenuItem(
					"rotate_clockwise",
					"Rotate Character Clock-Wise",
					new RotateSelectionCommand(RotateSelectionCommand.CLOCKWISE))),
			new SubToolbarButtonDefinition(new ToolbarButtonOfMenuItem(
					"rotate_counter_clockwise",
					"Rotate Character Counter Clock-Wise",
					new RotateSelectionCommand(RotateSelectionCommand.COUNTER_CLOCKWISE))));

	private final SubToolbarButtonDefinition[] buttons;

	SubToolbarsDefinitions(final SubToolbarButtonDefinition... buttons) {
		this.buttons = buttons;
	}

}
