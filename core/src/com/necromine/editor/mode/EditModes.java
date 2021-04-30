package com.necromine.editor.mode;

import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.ElementDefinition;
import com.gadarts.necromine.model.EnvironmentDefinitions;
import com.gadarts.necromine.model.MapNodeData;
import com.gadarts.necromine.model.characters.CharacterTypes;
import com.gadarts.necromine.model.pickups.WeaponsDefinitions;
import com.necromine.editor.EntriesDisplayTypes;
import com.necromine.editor.TreeSection;
import com.necromine.editor.actions.processes.MappingProcess;
import com.necromine.editor.handlers.action.ActionsHandler;
import com.necromine.editor.mode.tools.EditorTool;
import com.necromine.editor.mode.tools.EnvTools;
import com.necromine.editor.mode.tools.TilesTools;
import com.necromine.editor.model.elements.*;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static com.necromine.editor.EntriesDisplayTypes.NONE;

@Getter
public enum EditModes implements EditorMode {
	TILES("Tiles", true, EntriesDisplayTypes.GALLERY, TilesTools.values(), new TilesOnTouchDownLeftEvent()),

	CHARACTERS("Characters",
			EntriesDisplayTypes.TREE,
			true,
			null,
			true,
			PlacedCharacter::new,
			null,
			new CharactersOnTouchDownLeftEvent(),
			new TreeSection("Player", CharacterTypes.PLAYER.getDefinitions(), "character"),
			new TreeSection("Enemies", CharacterTypes.ENEMY.getDefinitions(), "character")),

	ENVIRONMENT("Environment Objects",
			EntriesDisplayTypes.TREE,
			EnvironmentDefinitions.values(),
			(params, assetsManager) -> new PlacedEnvObject(new PlacedModelElement.PlacedModelElementParameters(params), assetsManager),
			EnvTools.values(),
			new EnvOnTouchDownEventLeft(),
			new TreeSection("Environment", EnvironmentDefinitions.values(), "env")),

	PICKUPS("Pick-Ups",
			EntriesDisplayTypes.TREE,
			WeaponsDefinitions.values(),
			(params, am) -> new PlacedPickup(new PlacedModelElement.PlacedModelElementParameters(params), am),
			null,
			new PickupsOnTouchDownEventLeft(),
			new TreeSection("Weapons", Arrays.stream(WeaponsDefinitions.values()).filter(def -> def.getModelDefinition() != null).collect(Collectors.toList()).toArray(new ElementDefinition[0]), "pickup")),

	LIGHTS("Lights",
			true,
			PlacedLight::new,
			new LightsOnTouchDownEventLeft());


	private final TreeSection[] treeSections;
	private final EntriesDisplayTypes entriesDisplayTypes;
	private final String displayName;
	private final boolean decalCursor;
	private final PlacedElementCreation creationProcess;
	private final ElementDefinition[] definitions;
	private final boolean skipGenericElementLoading;
	private final EditorTool[] tools;
	private final OnTouchDownLeftEvent onTouchDownLeft;

	EditModes(final String displayName,
			  final boolean decalCursor,
			  final PlacedElementCreation creation,
			  final OnTouchDownLeftEvent onTouchDownLeftEvent) {
		this(displayName,
				NONE,
				decalCursor,
				null,
				false,
				creation,
				null,
				onTouchDownLeftEvent,
				(TreeSection[]) null);
	}

	EditModes(final String displayName,
			  final boolean skipGenericElementLoading,
			  final EntriesDisplayTypes type,
			  final EditorTool[] tools,
			  final OnTouchDownLeftEvent onTouchDownLeftEvent) {
		this(displayName, type, false, null, skipGenericElementLoading, null, tools, onTouchDownLeftEvent);
	}

	EditModes(final String displayName,
			  final EntriesDisplayTypes entriesDisplay,
			  final ElementDefinition[] definitions,
			  final PlacedElementCreation creation,
			  final EditorTool[] tools,
			  final OnTouchDownLeftEvent onTouchDownLeftEvent,
			  final TreeSection... treeSections) {
		this(displayName,
				entriesDisplay,
				false,
				definitions,
				false,
				creation,
				tools,
				onTouchDownLeftEvent,
				treeSections);
	}

	EditModes(final String displayName,
			  final EntriesDisplayTypes entriesDisplay,
			  final boolean decalCursor,
			  final ElementDefinition[] definitions,
			  final boolean skipGenericElementLoading,
			  final PlacedElementCreation creation,
			  final EditorTool[] tools,
			  final OnTouchDownLeftEvent onTouchDownLeftEvent,
			  final TreeSection... treeSections) {
		this.entriesDisplayTypes = entriesDisplay;
		this.displayName = displayName;
		this.decalCursor = decalCursor;
		this.creationProcess = creation;
		this.skipGenericElementLoading = skipGenericElementLoading;
		this.definitions = definitions;
		this.tools = tools;
		this.onTouchDownLeft = onTouchDownLeftEvent;
		this.treeSections = treeSections;
	}

	@Override
	public void onTouchDownLeft(final MappingProcess<? extends MappingProcess.FinishProcessParameters> currentProcess,
								final ActionsHandler actionsHandler,
								final GameAssetsManager assetsManager,
								final Set<MapNodeData> initializedTiles) {
		onTouchDownLeft.run(currentProcess, actionsHandler, assetsManager, initializedTiles);
	}
}
