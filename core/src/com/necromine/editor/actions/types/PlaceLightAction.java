package com.necromine.editor.actions.types;

import com.badlogic.gdx.graphics.Texture;
import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.ElementDefinition;
import com.gadarts.necromine.model.characters.Direction;
import com.necromine.editor.MapNode;
import com.necromine.editor.actions.PlaceElementAction;
import com.necromine.editor.model.PlacedLight;

import java.util.List;

public class PlaceLightAction extends PlaceElementAction<PlacedLight, ElementDefinition> {

	public PlaceLightAction(final MapNode[][] map,
							final List<PlacedLight> placedElements,
							final int selectedRow,
							final int selectedCol,
							final ElementDefinition selectedCharacter,
							final GameAssetsManager assetsManager) {
		super(map, selectedRow, selectedCol, assetsManager, Direction.SOUTH, selectedCharacter, placedElements);
	}

	@Override
	protected void execute() {
		MapNode tile = map[selectedRow][selectedCol];
		if (tile != null) {
			Texture texture = assetsManager.getTexture(Assets.UiTextures.BULB);
			placedElements.add(new PlacedLight(selectedRow, selectedCol, elementDefinition, texture));
		}
	}

	@Override
	public boolean isProcess() {
		return false;
	}
}
