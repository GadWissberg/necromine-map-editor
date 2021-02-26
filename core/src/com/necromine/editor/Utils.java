package com.necromine.editor;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.characters.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

import static com.gadarts.necromine.model.characters.CharacterTypes.BILLBOARD_SCALE;
import static com.gadarts.necromine.model.characters.CharacterTypes.BILLBOARD_Y;

public class Utils {
	static final String FRAMES_KEY_CHARACTER = "frames/%s";
	private final static Plane auxPlane = new Plane();
	private static final Vector3 auxVector3_1 = new Vector3();
	private static final Vector3 auxVector3_2 = new Vector3();

	public static CharacterDecal createCharacterDecal(final GameAssetsManager assetsManager,
													  final CharacterDefinition definition,
													  final Node node,
													  final Direction selectedCharacterDirection) {
		String idle = SpriteType.IDLE.name() + "_" + selectedCharacterDirection.name().toLowerCase();
		TextureAtlas atlas = assetsManager.getAtlas(definition.getAtlasDefinition());
		TextureAtlas.AtlasRegion region = atlas.findRegion(idle.toLowerCase());
		Decal decal = Decal.newDecal(region, true);
		decal.setPosition(node.getCol() + 0.5f, BILLBOARD_Y, node.getRow() + 0.5f);
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

	public static void initializeTile(final MapNode tile,
									  final Assets.FloorsTextures selectedTile,
									  final GameAssetsManager assetsManager) {
		tile.setTextureDefinition(selectedTile);
		Material material = tile.getModelInstance().materials.get(0);
		TextureAttribute textureAttribute = (TextureAttribute) material.get(TextureAttribute.Diffuse);
		textureAttribute.textureDescription.texture = assetsManager.getTexture(selectedTile);
	}

	public static Vector3 castRayTowardsPlane(final float screenX, final float screenY, final OrthographicCamera camera) {
		Ray ray = camera.getPickRay(screenX, screenY);
		auxPlane.set(Vector3.Zero, auxVector3_1.set(0, 1, 0));
		Intersector.intersectRayPlane(ray, auxPlane, auxVector3_2);
		return auxVector3_2;
	}

}
