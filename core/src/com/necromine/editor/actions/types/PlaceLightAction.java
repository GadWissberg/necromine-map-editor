package com.necromine.editor.actions.types;

import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector3;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.ElementDefinition;
import com.gadarts.necromine.model.characters.Direction;
import com.gadarts.necromine.model.map.MapNodeData;
import com.necromine.editor.GameMap;
import com.necromine.editor.actions.PlaceElementAction;
import com.necromine.editor.model.elements.PlacedElement.PlacedElementParameters;
import com.necromine.editor.model.elements.PlacedLight;

import java.util.List;

public class PlaceLightAction extends PlaceElementAction<PlacedLight, ElementDefinition> {

	public PlaceLightAction(final GameMap map,
							final List<PlacedLight> placedElements,
							final MapNodeData node,
							final ElementDefinition selectedCharacter,
							final GameAssetsManager assetsManager) {
		super(map, node, assetsManager, Direction.SOUTH, selectedCharacter, placedElements);
	}

	@Override
	protected void addElementToList(final PlacedLight element) {
		placedElements.add(element);
	}

	@Override
	protected void placeElementInCorrectHeight(final PlacedLight element, final MapNodeData tile) {
		Decal decal = element.getDecal();
		Vector3 position = decal.getPosition();
		decal.setPosition(position.x, tile.getHeight(), position.z);
	}

	@Override
	protected PlacedLight createElement(final MapNodeData tile) {
		PlacedLight result = null;
		if (tile != null) {
			result = new PlacedLight(new PlacedElementParameters(elementDefinition, node, 0), assetsManager);
		}
		return result;
	}

	@Override
	public boolean isProcess() {
		return false;
	}
}
