package com.necromine.editor.actions.types;

import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.ElementDefinition;
import com.gadarts.necromine.model.MapNodeData;
import com.gadarts.necromine.model.characters.Direction;
import com.necromine.editor.GameMap;
import com.necromine.editor.model.node.Node;
import com.necromine.editor.actions.PlaceElementAction;
import com.necromine.editor.model.elements.PlacedLight;

import java.util.List;

public class PlaceLightAction extends PlaceElementAction<PlacedLight, ElementDefinition> {

	public PlaceLightAction(final GameMap map,
							final List<PlacedLight> placedElements,
							final Node node,
							final ElementDefinition selectedCharacter,
							final GameAssetsManager assetsManager) {
		super(map, node, assetsManager, Direction.SOUTH, selectedCharacter, placedElements);
	}

	@Override
	protected void execute() {
		super.execute();
		MapNodeData tile = map.getNodes()[node.getRow()][node.getCol()];
		if (tile != null) {
			placedElements.add(new PlacedLight(node.getRow(), node.getCol(), elementDefinition, assetsManager));
		}
	}

	@Override
	public boolean isProcess() {
		return false;
	}
}
