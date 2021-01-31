package com.necromine.editor.model;

import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.characters.Direction;
import com.gadarts.necromine.model.pickups.ItemDefinition;

public class PlacedPickup extends PlacedModelElement {
	public PlacedPickup(final int row,
						final int col,
						final ItemDefinition definition,
						final GameAssetsManager assetsManager) {
		super(row, col, definition, Direction.SOUTH, assetsManager);
	}
}
