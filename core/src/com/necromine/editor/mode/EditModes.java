package com.necromine.editor.mode;

import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.ElementDefinition;
import com.gadarts.necromine.model.env.EnvironmentDefinitions;
import com.gadarts.necromine.model.map.MapNodeData;
import com.gadarts.necromine.model.pickups.WeaponsDefinitions;
import com.necromine.editor.actions.processes.MappingProcess;
import com.necromine.editor.handlers.action.ActionsHandler;
import com.necromine.editor.mode.events.*;
import com.necromine.editor.mode.tools.EditorTool;
import com.necromine.editor.mode.tools.ElementTools;
import com.necromine.editor.mode.tools.TilesTools;
import com.necromine.editor.model.elements.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Getter
@RequiredArgsConstructor
public enum EditModes implements EditorMode {
	TILES("Tiles Mode", true, TilesTools.values(), new TilesOnTouchDownLeftEvent()),

	CHARACTERS("Characters Mode",
			true,
			PlacedCharacter::new,
			null,
			true,
			ElementTools.values(),
			new CharactersOnTouchDownLeftEvent()),

	ENVIRONMENT("Environment Objects Mode", EnvironmentDefinitions.values(),
			(params, assetsManager) -> new PlacedEnvObject(
					new PlacedModelElement.PlacedModelElementParameters(params),
					assetsManager),
			ElementTools.values(),
			new EnvOnTouchDownEventLeft()),

	PICKUPS("Pickups Mode", WeaponsDefinitions.values(),
			(params, am) -> new PlacedPickup(new PlacedModelElement.PlacedModelElementParameters(params), am),
			ElementTools.values(),
			new PickupsOnTouchDownEventLeft()),

	LIGHTS("Lights Mode", true,
			PlacedLight::new,
			new LightsOnTouchDownEventLeft());
	private final String displayName;
	private final boolean decalCursor;
	private final PlacedElementCreation creationProcess;
	private final ElementDefinition[] definitions;
	private final boolean skipGenericElementLoading;
	private final EditorTool[] tools;
	private final OnTouchDownLeftEvent onTouchDownLeft;

	EditModes(String displayName,
			  boolean decalCursor,
			  PlacedElementCreation creation,
			  OnTouchDownLeftEvent onTouchDownLeftEvent) {
		this(displayName,
				decalCursor,
				creation,
				null,
				false,
				null,
				onTouchDownLeftEvent);
	}

	EditModes(String displayName,
			  boolean skipGenericElementLoading,
			  EditorTool[] tools,
			  OnTouchDownLeftEvent onTouchDownLeftEvent) {
		this(displayName,
				false,
				null,
				null,
				skipGenericElementLoading,
				tools,
				onTouchDownLeftEvent);
	}

	EditModes(String displayName,
			  ElementDefinition[] definitions,
			  PlacedElementCreation creation,
			  EditorTool[] tools,
			  OnTouchDownLeftEvent onTouchDownLeftEvent) {
		this(displayName,
				false,
				creation,
				definitions,
				false,
				tools,
				onTouchDownLeftEvent);
	}


	@Override
	public void onTouchDownLeft(final MappingProcess<? extends MappingProcess.FinishProcessParameters> currentProcess,
								final ActionsHandler actionsHandler,
								final GameAssetsManager assetsManager,
								final Set<MapNodeData> initializedTiles) {
		onTouchDownLeft.run(currentProcess, actionsHandler, assetsManager, initializedTiles);
	}

	@Override
	public String getDisplayName( ) {
		return displayName;
	}

}
