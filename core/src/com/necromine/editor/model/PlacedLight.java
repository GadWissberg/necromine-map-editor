package com.necromine.editor.model;

import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.ElementDefinition;
import com.gadarts.necromine.model.characters.Direction;
import com.necromine.editor.Node;
import com.necromine.editor.Utils;
import lombok.Getter;

@Getter
public class PlacedLight extends PlacedElement {
	private static final float BULB_Y = 0.5f;
	private final Decal decal;

	public PlacedLight(final int row,
					   final int col,
					   final ElementDefinition definition,
					   final GameAssetsManager gameAssetsManager) {
		super(new Node(row, col), definition, Direction.SOUTH);
		decal = Utils.createSimpleDecal(gameAssetsManager.getTexture(Assets.UiTextures.BULB));
		decal.setPosition(col + 0.5f, BULB_Y, row + 0.5f);
	}
}
