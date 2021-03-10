package com.necromine.editor.handlers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.EnvironmentDefinitions;
import com.gadarts.necromine.model.MapNodeData;
import com.gadarts.necromine.model.characters.CharacterTypes;
import com.gadarts.necromine.model.characters.Direction;
import com.necromine.editor.*;
import com.necromine.editor.mode.EditModes;
import com.necromine.editor.mode.EditorMode;
import com.necromine.editor.model.elements.CharacterDecal;
import com.necromine.editor.model.node.Node;
import com.necromine.editor.utils.Utils;
import lombok.Getter;
import lombok.Setter;

import static com.gadarts.necromine.model.characters.CharacterTypes.BILLBOARD_Y;
import static com.gadarts.necromine.model.characters.Direction.NORTH;
import static com.gadarts.necromine.model.characters.Direction.SOUTH;
import static com.necromine.editor.MapEditor.LEVEL_SIZE;

@Getter
@Setter
public class CursorHandler implements Disposable {
	public static final float FLICKER_RATE = 0.05f;
	public static final float CURSOR_Y = 0.01f;
	public static final float CURSOR_OPACITY = 0.5f;
	private static final Color CURSOR_COLOR = Color.valueOf("#2AFF14");
	private static final Vector3 auxVector3_1 = new Vector3();
	private static final Vector3 auxVector3_2 = new Vector3();
	private static final Vector3 auxVector3_3 = new Vector3();
	private ModelInstance cursorTileModelInstance;
	private CharacterDecal cursorCharacterDecal;
	private CursorSelectionModel cursorSelectionModel;
	private Model cursorTileModel;
	private ModelInstance highlighter;
	private Decal cursorSimpleDecal;
	private float flicker;

	public void updateCursorFlicker(final EditorMode mode) {
		Material material;
		if ((mode == EditModes.TILES || mode == EditModes.CHARACTERS)) {
			material = getHighlighter().materials.get(0);
		} else {
			material = getCursorTileModelInstance().materials.get(0);
		}
		BlendingAttribute blend = (BlendingAttribute) material.get(BlendingAttribute.Type);
		blend.opacity = Math.abs(MathUtils.sin(flicker += FLICKER_RATE));
		material.set(blend);
	}

	public boolean updateCursorByScreenCoords(final int screenX,
											  final int screenY,
											  final OrthographicCamera camera,
											  final GameMap map) {
		if (highlighter != null) {
			Vector3 collisionPoint = Utils.castRayTowardsPlane(screenX, screenY, camera);
			int x = MathUtils.clamp((int) collisionPoint.x, 0, LEVEL_SIZE - 1);
			int z = MathUtils.clamp((int) collisionPoint.z, 0, LEVEL_SIZE - 1);
			MapNodeData mapNodeData = map.getNodes()[z][x];
			highlighter.transform.setTranslation(x, (mapNodeData != null ? mapNodeData.getHeight() : 0) + 0.01f, z);
			updateCursorAdditionals(x, z, MapEditor.getMode());
			return true;
		}
		return false;
	}

	public void renderRectangleMarking(final int srcRow, final int srcCol, final ModelBatch modelBatch) {
		Vector3 initialTilePos = highlighter.transform.getTranslation(auxVector3_1);
		for (int i = Math.min((int) initialTilePos.x, srcCol); i <= Math.max((int) initialTilePos.x, srcCol); i++) {
			for (int j = Math.min((int) initialTilePos.z, srcRow); j <= Math.max((int) initialTilePos.z, srcRow); j++) {
				highlighter.transform.setTranslation(i, initialTilePos.y, j);
				modelBatch.render(highlighter);
			}
		}
		highlighter.transform.setTranslation(initialTilePos);
	}

	private void updateCursorOfDecalMode(final int x, final int z, final EditorMode mode) {
		if (mode == EditModes.CHARACTERS) {
			cursorCharacterDecal.getDecal().setPosition(x + 0.5f, BILLBOARD_Y, z + 0.5f);
		} else {
			cursorSimpleDecal.setPosition(x + 0.5f, BILLBOARD_Y, z + 0.5f);
		}
	}

	private void updateCursorAdditionals(final int x, final int z, final EditorMode mode) {
		ModelInstance modelInstance = cursorSelectionModel.getModelInstance();
		if (modelInstance != null) {
			modelInstance.transform.setTranslation(x, 0.01f, z);
		}
		updateCursorOfDecalMode(x, z, mode);
	}

	public void createCursors(final GameAssetsManager assetsManager, final Model tileModel) {
		this.cursorTileModel = tileModel;
		createCursorTile();
		createCursorCharacterDecal(assetsManager);
		createCursorSimpleDecal(assetsManager);
	}


	private void createCursorTile() {
		cursorTileModel.materials.get(0).set(ColorAttribute.createDiffuse(CURSOR_COLOR));
		cursorTileModelInstance = new ModelInstance(cursorTileModel);
	}

	public void renderModelCursorFloorGrid(final EnvironmentDefinitions selectedElement, final ModelBatch modelBatch) {
		Vector3 originalPosition = cursorTileModelInstance.transform.getTranslation(auxVector3_1);
		Vector3 cursorPosition = highlighter.transform.getTranslation(auxVector3_3);
		cursorPosition.y = CURSOR_Y;
		Direction facingDirection = cursorSelectionModel.getFacingDirection();
		renderModelCursorFloorGridCells(cursorPosition, selectedElement, facingDirection, modelBatch);
		cursorTileModelInstance.transform.setTranslation(originalPosition);
	}

	private void renderModelCursorFloorGridCells(final Vector3 cursorPosition,
												 final EnvironmentDefinitions def,
												 final Direction facingDirection,
												 final ModelBatch modelBatch) {
		int halfWidth = def.getWidth() / 2;
		int halfDepth = def.getDepth() / 2;
		if (facingDirection == NORTH || facingDirection == SOUTH) {
			int swap = halfWidth;
			halfWidth = halfDepth;
			halfDepth = swap;
		}
		for (int row = -halfDepth; row < Math.max(halfDepth, 1); row++) {
			renderModelCursorFloorGridRow(cursorPosition, halfWidth, row, modelBatch);
		}
	}

	private void renderModelCursorFloorGridRow(final Vector3 cursorPosition,
											   final int halfWidth,
											   final int row,
											   final ModelBatch modelBatch) {
		for (int col = -halfWidth; col < Math.max(halfWidth, 1); col++) {
			cursorTileModelInstance.transform.setTranslation(cursorPosition).translate(col, 0, row);
			modelBatch.render(cursorTileModelInstance);
		}
	}

	private void createCursorCharacterDecal(final GameAssetsManager assetsManager) {
		cursorCharacterDecal = Utils.createCharacterDecal(
				assetsManager,
				CharacterTypes.PLAYER.getDefinitions()[0],
				new Node(0, 0),
				SOUTH);
		Color color = cursorCharacterDecal.getDecal().getColor();
		cursorCharacterDecal.getDecal().setColor(color.r, color.g, color.b, CURSOR_OPACITY);
	}

	private void createCursorSimpleDecal(final GameAssetsManager assetsManager) {
		Texture bulb = assetsManager.getTexture(Assets.UiTextures.BULB);
		cursorSimpleDecal = Utils.createSimpleDecal(bulb);
		Color color = cursorSimpleDecal.getColor();
		cursorSimpleDecal.setColor(color.r, color.g, color.b, CURSOR_OPACITY);
	}

	@Override
	public void dispose() {
	}
}
