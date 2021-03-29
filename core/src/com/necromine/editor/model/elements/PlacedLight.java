package com.necromine.editor.model.elements;

import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.MapNodeData;
import com.necromine.editor.utils.Utils;
import lombok.Getter;

@Getter
public class PlacedLight extends PlacedElement {
	private static final float BULB_Y = 0.5f;
	private final Decal decal;

	public PlacedLight(final PlacedElementParameters params, final GameAssetsManager gameAssetsManager) {
		super(params);
		decal = Utils.createSimpleDecal(gameAssetsManager.getTexture(Assets.UiTextures.BULB));
		MapNodeData node = params.getNode();
		decal.setPosition(node.getCol() + 0.5f, BULB_Y, node.getRow() + 0.5f);
	}
}
