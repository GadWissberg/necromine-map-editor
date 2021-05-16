package com.necromine.editor.handlers.action;

import com.badlogic.gdx.graphics.g3d.Model;
import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.ElementDefinition;
import com.gadarts.necromine.model.MapNodeData;
import com.necromine.editor.actions.processes.MappingProcess;
import com.necromine.editor.model.elements.PlacedEnvObject;
import com.necromine.editor.model.node.FlatNode;
import com.necromine.editor.model.node.NodeWallsDefinitions;

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

	void setSelectedElement(ElementDefinition selectedElement);

	MappingProcess<? extends MappingProcess.FinishProcessParameters> getCurrentProcess();

	void onNodeWallsDefined(NodeWallsDefinitions definitions,
							FlatNode src,
							FlatNode dst);

	void onTilesLift(FlatNode src, FlatNode dst, float value);

	void onEnvObjectDefined(PlacedEnvObject element, float height);

	boolean onTouchDown(GameAssetsManager assetsManager, Set<MapNodeData> placedTiles, int button);

	boolean onTouchUp(Assets.FloorsTextures selectedTile, Model cursorTileModel);
}
