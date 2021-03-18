package com.necromine.editor.actions.types;

import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.MapNodeData;
import com.gadarts.necromine.model.characters.Direction;
import com.gadarts.necromine.model.pickups.ItemDefinition;
import com.necromine.editor.GameMap;
import com.necromine.editor.actions.PlaceElementAction;
import com.necromine.editor.model.elements.PlacedPickup;
import com.necromine.editor.model.node.Node;

import java.util.List;

public class PlacePickupAction extends PlaceElementAction<PlacedPickup, ItemDefinition> {

	public PlacePickupAction(final GameMap map,
							 final List<PlacedPickup> placedPickups,
							 final Node node,
							 final ItemDefinition itemDefinition,
							 final GameAssetsManager assetsManager,
							 final Direction facingDirection) {
		super(map, node, assetsManager, facingDirection, itemDefinition, placedPickups);
	}


	@Override
	protected void addElementToList(PlacedPickup element) {
		placedElements.add(element);
	}

	@Override
	protected void placeElementInCorrectHeight(PlacedPickup element, MapNodeData tile) {
		element.getModelInstance().transform.translate(0, tile.getHeight(), 0);
	}

	@Override
	protected PlacedPickup createElement(MapNodeData tile) {
		PlacedPickup result = null;
		if (tile != null) {
			result = new PlacedPickup(tile.getRow(), tile.getCol(), elementDefinition, assetsManager);
		}
		return result;
	}

	@Override
	public boolean isProcess() {
		return false;
	}
}
