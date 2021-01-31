package com.necromine.editor;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.characters.*;

import java.util.HashMap;

import static com.gadarts.necromine.model.characters.CharacterTypes.BILLBOARD_SCALE;
import static com.gadarts.necromine.model.characters.CharacterTypes.BILLBOARD_Y;

public class Utils {
	static final String FRAMES_KEY_CHARACTER = "frames/%s";

	public static CharacterDecal createCharacterDecal(final GameAssetsManager assetsManager,
													  final CharacterDefinition definition,
													  final int row,
													  final int col,
													  final Direction selectedCharacterDirection) {
		String idle = SpriteType.IDLE.name() + "_" + selectedCharacterDirection.name().toLowerCase();
		TextureAtlas atlas = assetsManager.getAtlas(definition.getAtlasDefinition());
		TextureAtlas.AtlasRegion region = atlas.findRegion(idle.toLowerCase());
		Decal decal = Decal.newDecal(region, true);
		decal.setPosition(col + 0.5f, BILLBOARD_Y, row + 0.5f);
		decal.setScale(BILLBOARD_SCALE);
		return new CharacterDecal(decal, definition, selectedCharacterDirection);
	}

	public static void applyFrameSeenFromCameraForCharacterDecal(final CharacterDecal characterDecal,
																 final Direction facingDirection,
																 final Camera camera,
																 final GameAssetsManager assetsManager) {
		Direction spriteDirection = characterDecal.getSpriteDirection();
		Direction dirSeenFromCamera = CharacterUtils.calculateDirectionSeenFromCamera(camera, facingDirection);
		if (dirSeenFromCamera != spriteDirection) {
			CharacterTypes characterType = characterDecal.getCharacterDefinition().getCharacterType();
			String name = String.format(FRAMES_KEY_CHARACTER, characterType.name());
			HashMap<Direction, TextureAtlas.AtlasRegion> hashMap = assetsManager.get(name);
			characterDecal.getDecal().setTextureRegion(hashMap.get(dirSeenFromCamera));
		}
	}
}
