package com.necromine.editor;

import com.gadarts.necromine.model.Enemies;
import lombok.Getter;

@Getter
public enum EditorModes {
	TILES,
	CHARACTERS(
			new ModeSection("Player", new PlayerDefinition[]{new PlayerDefinition()}),
			new ModeSection("Enemies", Enemies.values()));

	private final ModeSection[] modeSections;

	EditorModes() {
		this((ModeSection[]) null);
	}

	EditorModes(final ModeSection... modeSections) {
		this.modeSections = modeSections;
	}
}
