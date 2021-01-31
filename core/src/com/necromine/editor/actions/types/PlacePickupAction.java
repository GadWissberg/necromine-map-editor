package com.necromine.editor.actions.types;

import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.characters.Direction;
import com.gadarts.necromine.model.pickups.ItemDefinition;
import com.necromine.editor.MapNode;
import com.necromine.editor.actions.PlaceElementAction;
import com.necromine.editor.model.PlacedPickup;

import java.util.List;

public class PlacePickupAction extends PlaceElementAction<PlacedPickup, ItemDefinition> {

	public PlacePickupAction(final MapNode[][] map,
							 final List<PlacedPickup> placedPickups,
							 final int selectedRow,
							 final int selectedCol,
							 final ItemDefinition itemDefinition,
							 final GameAssetsManager assetsManager,
							 final Direction facingDirection) {
		super(map, selectedRow, selectedCol, assetsManager, facingDirection, itemDefinition, placedPickups);
	}

	@Override
	protected void execute() {
		MapNode tile = map[selectedRow][selectedCol];
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
