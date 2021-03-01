package com.necromine.editor.actions.types;

import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.characters.Direction;
import com.gadarts.necromine.model.pickups.ItemDefinition;
import com.necromine.editor.GameMap;
import com.necromine.editor.model.node.MapNode;
import com.necromine.editor.model.node.Node;
import com.necromine.editor.actions.PlaceElementAction;
import com.necromine.editor.model.elements.PlacedPickup;

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
	protected void execute() {
		super.execute();
		int selectedRow = node.getRow();
		int selectedCol = node.getCol();
		MapNode tile = map.getNodes()[selectedRow][selectedCol];
		if (tile != null) {
			PlacedPickup character = new PlacedPickup(selectedRow, selectedCol, elementDefinition, assetsManager);
			placedElements.add(character);
		}
	}

	@Override
	public boolean isProcess() {
		return false;
	}
}
