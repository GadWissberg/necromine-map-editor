package com.necromine.editor;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.MapNodesTypes;
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

	public static Decal createSimpleDecal(final Texture texture) {
		Decal decal = Decal.newDecal(new TextureRegion(texture), true);
		decal.setScale(BILLBOARD_SCALE);
		return decal;
	}

	public static MapNode createAndAddTileIfNotExists(final MapNode[][] map,
													  final int row,
													  final int col,
													  final Model tileModel,
													  final Assets.FloorsTextures selectedTile,
													  final GameAssetsManager assetsManager) {
		if (map[row][col] == null) {
			map[row][col] = new MapNode(tileModel, row, col, MapNodesTypes.PASSABLE_NODE);
		}
		MapNode tile = map[row][col];
		tile.setTextureDefinition(selectedTile);
		Material material = tile.getModelInstance().materials.get(0);
		TextureAttribute textureAttribute = (TextureAttribute) material.get(TextureAttribute.Diffuse);
		textureAttribute.textureDescription.texture = assetsManager.getTexture(selectedTile);
		return tile;
	}
}
