package com.necromine.editor.actions.types;

import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.EnvironmentDefinitions;
import com.gadarts.necromine.model.MapNodeData;
import com.gadarts.necromine.model.characters.Direction;
import com.necromine.editor.GameMap;
import com.necromine.editor.MapManagerEventsNotifier;
import com.necromine.editor.actions.PlaceElementAction;
import com.necromine.editor.model.elements.PlacedEnvObject;
import com.necromine.editor.model.node.Node;

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
	public void execute(MapManagerEventsNotifier eventsNotifier) {
		super.execute(eventsNotifier);
		applyOnMap();
	}

	@Override
	protected void addElementToList(PlacedEnvObject element) {
		placedEnvObjects.add(element);
	}

	@Override
	protected void placeElementInCorrectHeight(PlacedEnvObject element, MapNodeData tile) {
		element.getModelInstance().transform.translate(0, node.getHeight(), 0);
	}

	@Override
	protected PlacedEnvObject createElement(MapNodeData tile) {
		return new PlacedEnvObject(
				selectedEnvObject,
				node,
				assetsManager,
				elementDirection);
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
		MapNodeData[][] nodes = map.getNodes();
		int currentRow = Math.min(Math.max(node.getRow() + row, 0), nodes.length);
		int currentCol = Math.min(Math.max(node.getCol() + col, 0), nodes[0].length);
		MapNodeData mapNodeData = nodes[currentRow][currentCol];
		if (mapNodeData == null) {
			nodes[currentRow][currentCol] = new MapNodeData(currentRow, currentCol, selectedEnvObject.getNodeType());
		} else {
			mapNodeData.setMapNodeType(selectedEnvObject.getNodeType());
		}
	}

	@Override
	public boolean isProcess() {
		return false;
	}
}
