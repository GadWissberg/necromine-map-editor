package com.necromine.editor;

import com.gadarts.necromine.model.characters.CharacterTypes;
import lombok.Getter;

@Getter
public enum EditorModes {
	TILES,
	CHARACTERS(
			new TreeSection("Player", CharacterTypes.PLAYER.getDefinitions()),
			new TreeSection("Enemies", CharacterTypes.ENEMY.getDefinitions()));

	private final TreeSection[] treeSections;

	EditorModes() {
		this((TreeSection[]) null);
	}

	EditorModes(final TreeSection... treeSections) {
		this.treeSections = treeSections;
	}
}
