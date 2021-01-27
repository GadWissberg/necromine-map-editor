package com.necromine.editor;

import com.gadarts.necromine.model.EnvironmentDefinitions;
import com.gadarts.necromine.model.characters.CharacterTypes;
import lombok.Getter;

@Getter
public enum EditorModes {
	TILES("Tiles"),
	CHARACTERS("Characters", true,
			new TreeSection("Player", CharacterTypes.PLAYER.getDefinitions(),"character"),
			new TreeSection("Enemies", CharacterTypes.ENEMY.getDefinitions(),"character")),
	ENVIRONMENT("Environment Objects", true,
			new TreeSection("Environment", EnvironmentDefinitions.values(), "env"));


	private final TreeSection[] treeSections;
	private final boolean isDisplayedByTree;
	private final String displayName;

	EditorModes(final String displayName) {
		this(displayName, false, (TreeSection[]) null);
	}

	EditorModes(final String displayName, final boolean isDisplayedByTree, final TreeSection... treeSections) {
		this.isDisplayedByTree = isDisplayedByTree;
		this.treeSections = treeSections;
		this.displayName = displayName;
	}
}
