package com.necromine.editor.actions;

import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.EnvironmentDefinitions;
import com.gadarts.necromine.model.MapNodesTypes;
import com.gadarts.necromine.model.characters.Direction;
import com.necromine.editor.PlacedEnvObject;
import com.necromine.editor.MapNode;

import java.util.List;

public class PlaceEnvObjectAction extends PlaceElementAction {

	private final EnvironmentDefinitions selectedEnvObject;
	private final List<PlacedEnvObject> placedEnvObjects;

	public PlaceEnvObjectAction(final MapNode[][] map,
								final List<PlacedEnvObject> placedEnvObjects,
								final int selectedRow,
								final int selectedCol,
								final EnvironmentDefinitions definition,
								final GameAssetsManager assetsManager,
								final Direction selectedCharacterDirection) {
		super(map, selectedRow, selectedCol, assetsManager, selectedCharacterDirection);
		this.selectedEnvObject = definition;
		this.placedEnvObjects = placedEnvObjects;
	}

	@Override
	protected void execute() {
		PlacedEnvObject character = new PlacedEnvObject(
				selectedEnvObject,
				selectedRow,
				selectedCol,
				assetsManager,
				selectedCharacterDirection);
		placedEnvObjects.add(character);
		applyOnMap();
	}

	private void applyOnMap() {
		int halfHeight = selectedEnvObject.getHeight() / 2;
		int halfWidth = selectedEnvObject.getWidth() / 2;
		for (int row = -halfHeight; row < Math.max(halfHeight, 1); row++) {
			for (int col = -halfWidth; col < Math.max(halfWidth, 1); col++) {
				applyOnNode(row, col);
			}
		}
	}

	private void applyOnNode(final int row, final int col) {
		int currentRow = this.selectedRow + row;
		int currentCol = this.selectedCol + col;
		MapNode mapNode = map[currentRow][currentCol];
		if (mapNode == null) {
			map[currentRow][currentCol] = new MapNode(currentRow, currentCol, selectedEnvObject.getNodeType());
		} else {
			mapNode.setModelInstance(null);
			mapNode.setMapNodeType(selectedEnvObject.getNodeType());
		}
	}

	@Override
	public boolean isProcess() {
		return false;
	}
}
