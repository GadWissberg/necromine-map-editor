package com.necromine.editor.actions.types;

import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.EnvironmentDefinitions;
import com.gadarts.necromine.model.MapNodeData;
import com.gadarts.necromine.model.characters.Direction;
import com.necromine.editor.GameMap;
import com.necromine.editor.MapEditor;
import com.necromine.editor.MapManagerEventsNotifier;
import com.necromine.editor.actions.PlaceElementAction;
import com.necromine.editor.model.elements.PlacedEnvObject;
import com.necromine.editor.model.elements.PlacedModelElement.PlacedModelElementParameters;

import java.util.List;

public class PlaceEnvObjectAction extends PlaceElementAction<PlacedEnvObject, EnvironmentDefinitions> {

	private final EnvironmentDefinitions selectedEnvObject;
	private final List<PlacedEnvObject> placedEnvObjects;

	public PlaceEnvObjectAction(final GameMap map,
								final List<PlacedEnvObject> placedEnvObjects,
								final MapNodeData node,
								final EnvironmentDefinitions definition,
								final GameAssetsManager assetsManager,
								final Direction selectedObjectDirection) {
		super(map, node, assetsManager, selectedObjectDirection, definition, placedEnvObjects);
		this.selectedEnvObject = definition;
		this.placedEnvObjects = placedEnvObjects;
	}

	@Override
	public void execute(final MapManagerEventsNotifier eventsNotifier) {
		super.execute(eventsNotifier);
		applyOnMap();
	}

	@Override
	protected void addElementToList(final PlacedEnvObject element) {
		placedEnvObjects.add(element);
	}

	@Override
	protected void placeElementInCorrectHeight(final PlacedEnvObject element, final MapNodeData tile) {
		element.getModelInstance().transform.translate(0, node.getHeight(), 0);
	}

	@Override
	protected PlacedEnvObject createElement(final MapNodeData tile) {
		PlacedModelElementParameters parameters = new PlacedModelElementParameters(
				selectedEnvObject,
				elementDirection,
				node,
				0);
		return new PlacedEnvObject(parameters, assetsManager);
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
		MapNodeData[][] tiles = map.getNodes();
		MapNodeData mapNodeData = tiles[currentRow][currentCol];
		if (mapNodeData == null) {
			tiles[currentRow][currentCol] = new MapNodeData(currentRow, currentCol, selectedEnvObject.getNodeType());
		} else {
			mapNodeData.setMapNodeType(selectedEnvObject.getNodeType());
		}
	}

	@Override
	public boolean isProcess() {
		return false;
	}
}
