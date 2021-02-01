package com.necromine.editor.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.gadarts.necromine.model.ElementDefinition;
import com.gadarts.necromine.model.characters.Direction;
import com.necromine.editor.Utils;
import lombok.Getter;

@Getter
public class PlacedLight extends PlacedElement {
	private static final float BULB_Y = 0.5f;
	private final Decal decal;

	public PlacedLight(final int row,
					   final int col,
					   final ElementDefinition definition,
					   final Texture texture) {
		super(row, col, definition, Direction.SOUTH);
		decal = Utils.createSimpleDecal(texture);
		decal.setPosition(col, BULB_Y, row);
	}
}
