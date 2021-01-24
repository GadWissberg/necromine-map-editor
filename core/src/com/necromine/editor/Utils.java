package com.necromine.editor;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.characters.Direction;
import com.gadarts.necromine.model.characters.SpriteType;

import static com.gadarts.necromine.model.characters.CharacterTypes.BILLBOARD_SCALE;
import static com.gadarts.necromine.model.characters.CharacterTypes.BILLBOARD_Y;

public class Utils {
	public static CharacterDecal createCharacterDecal(final GameAssetsManager assetsManager,
													  final Assets.Atlases atlas,
													  final int row,
													  final int col) {
		String idle = SpriteType.IDLE.name() + "_" + Direction.SOUTH;
		TextureAtlas.AtlasRegion region = assetsManager.getAtlas(atlas).findRegion(idle.toLowerCase());
		Decal decal = Decal.newDecal(region, true);
		decal.setPosition(col + 0.5f, BILLBOARD_Y, row + 0.5f);
		decal.setScale(BILLBOARD_SCALE);
		return new CharacterDecal(decal);
	}
}
