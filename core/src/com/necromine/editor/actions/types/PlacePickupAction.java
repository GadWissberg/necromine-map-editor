package com.necromine.editor.actions.types;

import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.MapNodeData;
import com.gadarts.necromine.model.characters.Direction;
import com.gadarts.necromine.model.pickups.ItemDefinition;
import com.necromine.editor.GameMap;
import com.necromine.editor.actions.PlaceElementAction;
import com.necromine.editor.model.elements.PlacedModelElement.PlacedModelElementParameters;
import com.necromine.editor.model.elements.PlacedPickup;

import java.util.List;

public class PlacePickupAction extends PlaceElementAction<PlacedPickup, ItemDefinition> {

	public PlacePickupAction(final GameMap map,
							 final List<PlacedPickup> placedPickups,
							 final MapNodeData node,
							 final ItemDefinition itemDefinition,
							 final GameAssetsManager assetsManager,
							 final Direction facingDirection) {
		super(map, node, assetsManager, facingDirection, itemDefinition, placedPickups);
	}


	@Override
	protected void addElementToList(final PlacedPickup element) {
		placedElements.add(element);
	}

	@Override
	protected void placeElementInCorrectHeight(final PlacedPickup element, final MapNodeData tile) {
		element.getModelInstance().transform.translate(0, tile.getHeight(), 0);
	}

	@Override
	protected PlacedPickup createElement(final MapNodeData node) {
		PlacedPickup result = null;
		if (node != null) {
			PlacedModelElementParameters parameters = new PlacedModelElementParameters(elementDefinition, node, 0);
			result = new PlacedPickup(parameters, assetsManager);
		}
		return result;
	}

	@Override
	public boolean isProcess() {
		return false;
	}
}
