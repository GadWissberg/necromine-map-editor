package com.necromine.editor.actions.types;

import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.Coords;
import com.gadarts.necromine.model.characters.Direction;
import com.gadarts.necromine.model.env.EnvironmentDefinitions;
import com.gadarts.necromine.model.map.MapNodeData;
import com.necromine.editor.GameMap;
import com.necromine.editor.MapEditorEventsNotifier;
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
	public void execute(final MapEditorEventsNotifier eventsNotifier) {
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
		MapNodeData[][] nodes = map.getNodes();
		Coords coords = node.getCoords();
		int currentRow = Math.min(Math.max(coords.getRow() + row, 0), nodes.length);
		int currentCol = Math.min(Math.max(coords.getCol() + col, 0), nodes[0].length);
		if (currentRow < nodes.length && currentCol < nodes[0].length) {
			if (nodes[currentRow][currentCol] == null) {
				nodes[currentRow][currentCol] = new MapNodeData(currentRow, currentCol, selectedEnvObject.getNodeType());
			} else {
				nodes[currentRow][currentCol].setMapNodeType(selectedEnvObject.getNodeType());
			}
		}
	}

	@Override
	public boolean isProcess() {
		return false;
	}
}
