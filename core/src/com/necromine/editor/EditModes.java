package com.necromine.editor;

import com.gadarts.necromine.model.ElementDefinition;
import com.gadarts.necromine.model.EnvironmentDefinitions;
import com.gadarts.necromine.model.characters.CharacterTypes;
import com.gadarts.necromine.model.pickups.WeaponsDefinitions;
import lombok.Getter;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.necromine.editor.EntriesDisplayTypes.NONE;

@Getter
public enum EditModes implements EditorMode {
	TILES("Tiles", EntriesDisplayTypes.GALLERY),

	CHARACTERS("Characters", EntriesDisplayTypes.TREE, true,
			new TreeSection("Player", CharacterTypes.PLAYER.getDefinitions(), "character"),
			new TreeSection("Enemies", CharacterTypes.ENEMY.getDefinitions(), "character")),

	ENVIRONMENT("Environment Objects", EntriesDisplayTypes.TREE,
			new TreeSection("Environment", EnvironmentDefinitions.values(), "env")),

	PICKUPS("Pick-Ups", EntriesDisplayTypes.TREE,
			new TreeSection(
					"Weapons",
					Arrays.stream(WeaponsDefinitions.values()).filter(def -> def.getModelDefinition() != null).collect(Collectors.toList()).toArray(new ElementDefinition[0]),
					"pickup")),

	LIGHTS("Lights", true);


	private final TreeSection[] treeSections;
	private final EntriesDisplayTypes entriesDisplayTypes;
	private final String displayName;
	private final boolean decalCursor;

	EditModes(final String displayName, final boolean decalCursor) {
		this(displayName, NONE, decalCursor, (TreeSection[]) null);
	}

	EditModes(final String displayName, final EntriesDisplayTypes type) {
		this(displayName, type, (TreeSection[]) null);
	}

	EditModes(final String displayName, final EntriesDisplayTypes entriesDisplay, final TreeSection... treeSections) {
		this(displayName, entriesDisplay, false, treeSections);
	}

	EditModes(final String displayName, final EntriesDisplayTypes entriesDisplay, final boolean decalCursor, final TreeSection... treeSections) {
		this.entriesDisplayTypes = entriesDisplay;
		this.treeSections = treeSections;
		this.displayName = displayName;
		this.decalCursor = decalCursor;
	}

}
