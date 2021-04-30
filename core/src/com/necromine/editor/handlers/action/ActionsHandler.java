package com.necromine.editor.handlers.action;

import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.ElementDefinition;
import com.gadarts.necromine.model.MapNodeData;

import java.util.Set;

public interface ActionsHandler {
	void beginTilePlacingProcess(final GameAssetsManager assetsManager, final Set<MapNodeData> initializedTiles);

	boolean beginSelectingTileForLiftProcess(int direction, Set<MapNodeData> initializedTiles);

	void beginSelectingTilesForWallTiling();

	ElementDefinition getSelectedElement();

	void placeEnvObject(GameAssetsManager assetsManager);

	void defineSelectedEnvObject();

	void placeLight(GameAssetsManager assetsManager);

	void placeCharacter(GameAssetsManager assetsManager);

	void placePickup(GameAssetsManager assetsManager);
}
