package com.necromine.editor.actions.types;

import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.EnvironmentDefinitions;
import com.gadarts.necromine.model.characters.Direction;
import com.necromine.editor.GameMap;
import com.necromine.editor.MapEditor;
import com.necromine.editor.model.node.Node;
import com.necromine.editor.model.elements.PlacedEnvObject;
import com.necromine.editor.model.node.MapNode;
import com.necromine.editor.actions.PlaceElementAction;

import java.util.List;

public class PlaceEnvObjectAction extends PlaceElementAction<PlacedEnvObject, EnvironmentDefinitions> {

	private final EnvironmentDefinitions selectedEnvObject;
	private final List<PlacedEnvObject> placedEnvObjects;

	public PlaceEnvObjectAction(final GameMap map,
								final List<PlacedEnvObject> placedEnvObjects,
								final Node node,
								final EnvironmentDefinitions definition,
								final GameAssetsManager assetsManager,
								final Direction selectedObjectDirection) {
		super(map, node, assetsManager, selectedObjectDirection, definition, placedEnvObjects);
		this.selectedEnvObject = definition;
		this.placedEnvObjects = placedEnvObjects;
	}

	@Override
	protected void execute() {
		super.execute();
		PlacedEnvObject env = new PlacedEnvObject(
				selectedEnvObject,
				node,
				assetsManager,
				elementDirection);
		placedEnvObjects.add(env);
		applyOnMap();
	}

	private void applyOnMap() {
		int halfHeight = selectedEnvObject.getDepth() / 2;
		int halfWidth = selectedEnvObject.getWidth() / 2;
		for (int row = -halfHeight; row < Math.max(halfHeight, 1); row++) {
			for (int col = -halfWidth; col < Math.max(halfWidth, 1); col++) {
				applyOnNode(row, col);
			}
		}
	}

	private void applyOnNode(final int row, final int col) {
		int currentRow = Math.min(Math.max(node.getRow() + row, 0), MapEditor.LEVEL_SIZE);
		int currentCol = Math.min(Math.max(node.getCol() + col, 0), MapEditor.LEVEL_SIZE);
		MapNode[][] tiles = map.getTiles();
		MapNode mapNode = tiles[currentRow][currentCol];
		if (mapNode == null) {
			tiles[currentRow][currentCol] = new MapNode(currentRow, currentCol, selectedEnvObject.getNodeType());
		} else {
			mapNode.setMapNodeType(selectedEnvObject.getNodeType());
		}
	}

	@Override
	public boolean isProcess() {
		return false;
	}
}
