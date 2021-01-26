package com.necromine.editor.actions;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.characters.CharacterDefinition;
import com.gadarts.necromine.model.characters.Direction;
import com.necromine.editor.EditorModes;
import com.necromine.editor.NecromineMapEditor;
import com.necromine.editor.PlacedCharacter;
import com.necromine.editor.Tile;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class ActionsHandler {
	private static final Vector3 auxVector = new Vector3();
	private final ModelInstance cursorTileModelInstance;
	private final Tile[][] map;
	private final List<PlacedCharacter> placedCharacters;

	@Setter
	private Assets.FloorsTextures selectedTile;

	@Setter
	private CharacterDefinition selectedCharacter;

	@Setter
	private Direction selectedCharacterDirection;

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
										 final Set<Tile> initializedTiles) {
		Vector3 position = cursorTileModelInstance.transform.getTranslation(auxVector);
		int row = (int) position.z;
		int col = (int) position.x;
		PlaceTilesProcess placeTilesProcess = new PlaceTilesProcess(row, col, assetsManager, initializedTiles, map);
		currentProcess = placeTilesProcess;
		placeTilesProcess.execute();
	}

	public boolean onTouchDown(final GameAssetsManager assetsManager,
							   final Set<Tile> initializedTiles) {
		EditorModes mode = NecromineMapEditor.getMode();
		if (mode == EditorModes.TILES && currentProcess == null && selectedTile != null) {
			beginTilePlacingProcess(cursorTileModelInstance, assetsManager, initializedTiles);
			return true;
		} else if (mode == EditorModes.CHARACTERS && selectedCharacter != null) {
			placeCharacter(cursorTileModelInstance, map, assetsManager);
			return true;
		}
		return false;
	}

	private void placeCharacter(final ModelInstance modelInstance,
								final Tile[][] map,
								final GameAssetsManager am) {
		Vector3 position = modelInstance.transform.getTranslation(auxVector);
		int row = (int) position.z;
		int col = (int) position.x;
		PlaceCharacterAction action = new PlaceCharacterAction(
				map,
				placedCharacters,
				row,
				col,
				selectedCharacter,
				am,
				selectedCharacterDirection);
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
