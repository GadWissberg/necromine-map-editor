package com.necromine.editor;

import com.gadarts.necromine.model.ElementDefinition;
import com.gadarts.necromine.model.EnvironmentDefinitions;
import com.gadarts.necromine.model.characters.CharacterTypes;
import com.gadarts.necromine.model.pickups.ItemDefinition;
import com.gadarts.necromine.model.pickups.WeaponsDefinitions;
import com.necromine.editor.model.PlacedEnvObject;
import com.necromine.editor.model.PlacedLight;
import com.necromine.editor.model.PlacedPickup;
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

	ENVIRONMENT("Environment Objects", EntriesDisplayTypes.TREE, EnvironmentDefinitions.values(),
			(def, node, dir, assetsManager) -> new PlacedEnvObject((EnvironmentDefinitions) def, node, assetsManager, dir),
			new TreeSection("Environment", EnvironmentDefinitions.values(), "env")),

	PICKUPS("Pick-Ups", EntriesDisplayTypes.TREE, WeaponsDefinitions.values(),
			(def, node, dir, am) -> new PlacedPickup(node.getRow(), node.getCol(), (ItemDefinition) def, am),
			new TreeSection("Weapons", Arrays.stream(WeaponsDefinitions.values()).filter(def -> def.getModelDefinition() != null).collect(Collectors.toList()).toArray(new ElementDefinition[0]), "pickup")),

	LIGHTS("Lights", true,
			(def, node, dir, assetsManager) -> new PlacedLight(node.getRow(), node.getCol(), def, assetsManager));


	private final TreeSection[] treeSections;
	private final EntriesDisplayTypes entriesDisplayTypes;
	private final String displayName;
	private final boolean decalCursor;
	private final PlacedElementCreation creationProcess;
	private final ElementDefinition[] definitions;

	EditModes(final String displayName, final boolean decalCursor, final PlacedElementCreation creation) {
		this(displayName, NONE, decalCursor, null, creation, (TreeSection[]) null);
	}

	EditModes(final String displayName, final EntriesDisplayTypes type) {
		this(displayName, type, false);
	}

	EditModes(final String displayName,
			  final EntriesDisplayTypes entriesDisplay,
			  final boolean decalCursor,
			  final TreeSection... treeSections) {
		this(displayName, entriesDisplay, decalCursor, null, null, treeSections);
	}

	EditModes(final String displayName,
			  final EntriesDisplayTypes entriesDisplay,
			  final ElementDefinition[] definitions,
			  final PlacedElementCreation creation,
			  final TreeSection... treeSections) {
		this(displayName, entriesDisplay, false, definitions, creation, treeSections);
	}

	EditModes(final String displayName,
			  final EntriesDisplayTypes entriesDisplay,
			  final boolean decalCursor,
			  final ElementDefinition[] definitions,
			  final PlacedElementCreation creation,
			  final TreeSection... treeSections) {
		this.entriesDisplayTypes = entriesDisplay;
		this.treeSections = treeSections;
		this.displayName = displayName;
		this.decalCursor = decalCursor;
		this.creationProcess = creation;
		this.definitions = definitions;
	}

}
