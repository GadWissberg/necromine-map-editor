package com.gadarts.necromine.editor.desktop;

import com.gadarts.necromine.editor.desktop.actions.RotateCharacterCommand;
import com.gadarts.necromine.editor.desktop.toolbar.ToolbarButtonOfMenuItem;
import com.necromine.editor.EditorModes;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum SubToolbarsDefinitions {
	MODE_CHARACTERS(EditorModes.CHARACTERS,
			new SubToolbarButtonDefinition(new ToolbarButtonOfMenuItem(
					"rotate_clockwise",
					"Rotate Character Clock-Wise",
					new RotateCharacterCommand(RotateCharacterCommand.CLOCKWISE))),
			new SubToolbarButtonDefinition(new ToolbarButtonOfMenuItem(
					"rotate_counter_clockwise",
					"Rotate Character Counter Clock-Wise",
					new RotateCharacterCommand(RotateCharacterCommand.COUNTER_CLOCKWISE))));

	private final SubToolbarButtonDefinition[] buttons;
	private final EditorModes relatedToMode;

	SubToolbarsDefinitions(final EditorModes relatedToMode, final SubToolbarButtonDefinition... buttons) {
		this.relatedToMode = relatedToMode;
		this.buttons = buttons;
	}

	public static SubToolbarButtonDefinition[] getButtonsOfMode(final EditorModes mode) {
		Optional<SubToolbarsDefinitions> result = Arrays.stream(values())
				.filter(subToolbarsDefinitions -> subToolbarsDefinitions.getRelatedToMode() == mode)
				.findFirst();
		return result.map(SubToolbarsDefinitions::getButtons).orElse(null);
	}
}
