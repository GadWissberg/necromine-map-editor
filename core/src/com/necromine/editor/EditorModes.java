package com.necromine.editor;

import com.gadarts.necromine.model.ElementDefinition;
import com.gadarts.necromine.model.EnvironmentDefinitions;
import com.gadarts.necromine.model.characters.CharacterTypes;
import com.gadarts.necromine.model.pickups.WeaponsDefinitions;
import lombok.Getter;

import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
public enum EditorModes {
	TILES("Tiles"),
	CHARACTERS("Characters", true,
			new TreeSection("Player", CharacterTypes.PLAYER.getDefinitions(), "character"),
			new TreeSection("Enemies", CharacterTypes.ENEMY.getDefinitions(), "character")),
	ENVIRONMENT("Environment Objects", true,
			new TreeSection("Environment", EnvironmentDefinitions.values(), "env")),
	PICKUPS("Pick-Ups", true,
			new TreeSection("Weapons", Arrays.stream(WeaponsDefinitions.values()).filter(def -> def.getModelDefinition() != null).collect(Collectors.toList()).toArray(new ElementDefinition[0]), "pickup"));


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
