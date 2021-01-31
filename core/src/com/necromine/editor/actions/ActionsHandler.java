package com.necromine.editor.actions;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.ElementDefinition;
import com.gadarts.necromine.model.EnvironmentDefinitions;
import com.gadarts.necromine.model.characters.CharacterDefinition;
import com.gadarts.necromine.model.pickups.ItemDefinition;
import com.necromine.editor.*;
import com.necromine.editor.actions.processes.MappingProcess;
import com.necromine.editor.actions.processes.PlaceTilesFinishProcessParameters;
import com.necromine.editor.actions.processes.PlaceTilesProcess;
import com.necromine.editor.actions.types.PlaceCharacterAction;
import com.necromine.editor.actions.types.PlaceEnvObjectAction;
import com.necromine.editor.actions.types.PlacePickupAction;
import com.necromine.editor.model.PlacedCharacter;
import com.necromine.editor.model.PlacedElement;
import com.necromine.editor.model.PlacedEnvObject;
import com.necromine.editor.model.PlacedPickup;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
public class ActionsHandler {
	private static final Vector3 auxVector = new Vector3();

	private final ModelInstance cursorTileModelInstance;
	private final MapNode[][] map;
	private final Map<EditorModes, List<? extends PlacedElement>> placedElements;

	@Setter
	private Assets.FloorsTextures selectedTile;

	@Setter
	private ElementDefinition selectedElement;

	@Setter
	private CharacterDecal cursorCharacterDecal;

	@Setter
	private CursorSelectionModel cursorSelectionModel;

	@Getter
	private MappingProcess<? extends MappingProcess.FinishProcessParameters> currentProcess;

	public void executeAction(final MappingAction mappingAction) {
		mappingAction.execute();
		if (mappingAction.isProcess()) {
			currentProcess = (MappingProcess<? extends MappingProcess.FinishProcessParameters>) mappingAction;
		}
	}

	private void beginTilePlacingProcess(final ModelInstance cursorTileModelInstance,
										 final GameAssetsManager assetsManager,
										 final Set<MapNode> initializedTiles) {
		Vector3 position = cursorTileModelInstance.transform.getTranslation(auxVector);
		int row = (int) position.z;
		int col = (int) position.x;
		PlaceTilesProcess placeTilesProcess = new PlaceTilesProcess(row, col, assetsManager, initializedTiles, map);
		currentProcess = placeTilesProcess;
		placeTilesProcess.execute();
	}

	public boolean onTouchDown(final GameAssetsManager assetsManager,
							   final Set<MapNode> initializedTiles) {
		EditorModes mode = NecromineMapEditor.getMode();
		if (mode == EditorModes.TILES && currentProcess == null && selectedTile != null) {
			beginTilePlacingProcess(cursorTileModelInstance, assetsManager, initializedTiles);
			return true;
		} else if (mode == EditorModes.CHARACTERS && selectedElement != null) {
			placeCharacter(cursorTileModelInstance, map, assetsManager);
			return true;
		} else if (mode == EditorModes.ENVIRONMENT && selectedElement != null) {
			placeEnvObject(cursorSelectionModel.getModelInstance(), map, assetsManager);
		} else if (mode == EditorModes.PICKUPS && selectedElement != null) {
			placePickup(cursorSelectionModel.getModelInstance(), map, assetsManager);
			return true;
		}
		return false;
	}

	private void placeEnvObject(final ModelInstance modelInstance,
								final MapNode[][] map,
								final GameAssetsManager am) {
		Vector3 position = modelInstance.transform.getTranslation(auxVector);
		int row = (int) position.z;
		int col = (int) position.x;
		PlaceEnvObjectAction action = new PlaceEnvObjectAction(
				map,
				(List<PlacedEnvObject>) placedElements.get(EditorModes.ENVIRONMENT),
				row,
				col,
				(EnvironmentDefinitions) selectedElement,
				am,
				cursorSelectionModel.getFacingDirection());
		executeAction(action);
	}

	private void placePickup(final ModelInstance modelInstance,
							 final MapNode[][] map,
							 final GameAssetsManager am) {
		Vector3 position = modelInstance.transform.getTranslation(auxVector);
		int row = (int) position.z;
		int col = (int) position.x;
		PlacePickupAction action = new PlacePickupAction(
				map,
				(List<PlacedPickup>) placedElements.get(EditorModes.PICKUPS),
				row,
				col,
				(ItemDefinition) selectedElement,
				am,
				cursorSelectionModel.getFacingDirection());
		executeAction(action);
	}

	private void placeCharacter(final ModelInstance modelInstance,
								final MapNode[][] map,
								final GameAssetsManager am) {
		Vector3 position = modelInstance.transform.getTranslation(auxVector);
		int row = (int) position.z;
		int col = (int) position.x;
		PlaceCharacterAction action = new PlaceCharacterAction(
				map,
				(List<PlacedCharacter>) placedElements.get(EditorModes.CHARACTERS),
				row,
				col,
				(CharacterDefinition) selectedElement,
				am,
				cursorCharacterDecal.getSpriteDirection());
		executeAction(action);
	}

	public boolean onTouchUp(final Assets.FloorsTextures selectedTile,
							 final Model cursorTileModel) {
		if (currentProcess != null) {
			return finishProcess(selectedTile, cursorTileModel);
		}
		return false;
	}

	private boolean finishProcess(final Assets.FloorsTextures selectedTile, final Model cursorTileModel) {
		Vector3 position = cursorTileModelInstance.transform.getTranslation(auxVector);
		PlaceTilesProcess currentProcess = (PlaceTilesProcess) this.currentProcess;
		int dstRow = (int) position.z;
		int dstCol = (int) position.x;
		currentProcess.finish(new PlaceTilesFinishProcessParameters(dstRow, dstCol, selectedTile, cursorTileModel));
		this.currentProcess = null;
		return true;
	}
}
