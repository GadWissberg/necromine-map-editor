package com.necromine.editor.mode;

import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.ElementDefinition;
import com.gadarts.necromine.model.env.EnvironmentDefinitions;
import com.gadarts.necromine.model.map.MapNodeData;
import com.gadarts.necromine.model.pickups.WeaponsDefinitions;
import com.necromine.editor.actions.processes.MappingProcess;
import com.necromine.editor.handlers.action.ActionsHandler;
import com.necromine.editor.mode.tools.EditorTool;
import com.necromine.editor.mode.tools.EnvTools;
import com.necromine.editor.mode.tools.TilesTools;
import com.necromine.editor.model.elements.PlacedCharacter;
import com.necromine.editor.model.elements.PlacedElementCreation;
import com.necromine.editor.model.elements.PlacedEnvObject;
import com.necromine.editor.model.elements.PlacedLight;
import com.necromine.editor.model.elements.PlacedModelElement;
import com.necromine.editor.model.elements.PlacedPickup;
import lombok.Getter;

import java.util.Set;

@Getter
public enum EditModes implements EditorMode {
	TILES(true, TilesTools.values(), new TilesOnTouchDownLeftEvent()),

	CHARACTERS(true,
			null,
			true,
			PlacedCharacter::new,
			null,
			new CharactersOnTouchDownLeftEvent()),

	ENVIRONMENT(EnvironmentDefinitions.values(),
			(params, assetsManager) -> new PlacedEnvObject(new PlacedModelElement.PlacedModelElementParameters(params), assetsManager),
			EnvTools.values(),
			new EnvOnTouchDownEventLeft()),

	PICKUPS(WeaponsDefinitions.values(),
			(params, am) -> new PlacedPickup(new PlacedModelElement.PlacedModelElementParameters(params), am),
			null,
			new PickupsOnTouchDownEventLeft()),

	LIGHTS(true,
			PlacedLight::new,
			new LightsOnTouchDownEventLeft());
	private final boolean decalCursor;
	private final PlacedElementCreation creationProcess;
	private final ElementDefinition[] definitions;
	private final boolean skipGenericElementLoading;
	private final EditorTool[] tools;
	private final OnTouchDownLeftEvent onTouchDownLeft;

	EditModes(final boolean decalCursor,
			  final PlacedElementCreation creation,
			  final OnTouchDownLeftEvent onTouchDownLeftEvent) {
		this(decalCursor,
				null,
				false,
				creation,
				null,
				onTouchDownLeftEvent);
	}

	EditModes(final boolean skipGenericElementLoading,
			  final EditorTool[] tools,
			  final OnTouchDownLeftEvent onTouchDownLeftEvent) {
		this(false, null, skipGenericElementLoading, null, tools, onTouchDownLeftEvent);
	}

	EditModes(final ElementDefinition[] definitions,
			  final PlacedElementCreation creation,
			  final EditorTool[] tools,
			  final OnTouchDownLeftEvent onTouchDownLeftEvent) {
		this(
				false,
				definitions,
				false,
				creation,
				tools,
				onTouchDownLeftEvent);
	}

	EditModes(final boolean decalCursor,
			  final ElementDefinition[] definitions,
			  final boolean skipGenericElementLoading,
			  final PlacedElementCreation creation,
			  final EditorTool[] tools,
			  final OnTouchDownLeftEvent onTouchDownLeftEvent) {
		this.decalCursor = decalCursor;
		this.creationProcess = creation;
		this.skipGenericElementLoading = skipGenericElementLoading;
		this.definitions = definitions;
		this.tools = tools;
		this.onTouchDownLeft = onTouchDownLeftEvent;
	}

	@Override
	public void onTouchDownLeft(final MappingProcess<? extends MappingProcess.FinishProcessParameters> currentProcess,
								final ActionsHandler actionsHandler,
								final GameAssetsManager assetsManager,
								final Set<MapNodeData> initializedTiles) {
		onTouchDownLeft.run(currentProcess, actionsHandler, assetsManager, initializedTiles);
	}
}
