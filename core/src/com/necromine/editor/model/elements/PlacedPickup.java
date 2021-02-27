package com.necromine.editor.model.elements;

import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.characters.Direction;
import com.gadarts.necromine.model.pickups.ItemDefinition;
import com.necromine.editor.model.node.Node;

public class PlacedPickup extends PlacedModelElement {
	public PlacedPickup(final int row,
						final int col,
						final ItemDefinition definition,
						final GameAssetsManager assetsManager) {
		super(new Node(row, col), definition, Direction.SOUTH, assetsManager);
	}
}
