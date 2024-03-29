package com.necromine.editor.handlers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.characters.CharacterDefinition;
import com.gadarts.necromine.model.characters.Direction;
import com.gadarts.necromine.model.env.EnvironmentDefinitions;
import com.necromine.editor.MapRendererImpl;
import com.necromine.editor.mode.EditModes;
import com.necromine.editor.mode.EditorMode;
import com.necromine.editor.model.GameMap;
import com.necromine.editor.model.elements.CharacterDecal;
import com.necromine.editor.model.node.FlatNode;
import com.necromine.editor.utils.Utils;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

import static com.gadarts.necromine.model.characters.CharacterTypes.BILLBOARD_Y;
import static com.gadarts.necromine.model.characters.Direction.NORTH;
import static com.gadarts.necromine.model.characters.Direction.SOUTH;


/**
 * Responsible to handle the cursor's model.
 */
@Getter
@Setter
public class CursorHandler implements Disposable {

	private static final float FLICKER_RATE = 0.05f;
	private static final float CURSOR_Y = 0.01f;
	private static final float CURSOR_OPACITY = 0.5f;
	private static final Vector3 auxVector3_1 = new Vector3();
	private static final Vector3 auxVector3_2 = new Vector3();
	private static final Vector3 auxVector3_3 = new Vector3();

	private CursorHandlerModelData cursorHandlerModelData = new CursorHandlerModelData();
	private CharacterDecal cursorCharacterDecal;
	private ModelInstance highlighter;
	private Decal cursorSimpleDecal;
	private float flicker;

	void applyOpacity( ) {
		ModelInstance modelInstance = cursorHandlerModelData.getCursorSelectionModel().getModelInstance();
		BlendingAttribute blend = (BlendingAttribute) modelInstance.materials.get(0).get(BlendingAttribute.Type);
		if (blend != null) {
			blend.opacity = CursorHandler.CURSOR_OPACITY;
		}
	}

	/**
	 * Takes step in the flicker effect.
	 *
	 * @param mode Current editor mode.
	 */
	public void updateCursorFlicker(final EditorMode mode) {
		Material material;
		if ((mode == EditModes.TILES || mode == EditModes.CHARACTERS)) {
			material = getHighlighter().materials.get(0);
		} else {
			material = cursorHandlerModelData.getCursorTileModelInstance().materials.get(0);
		}
		BlendingAttribute blend = (BlendingAttribute) material.get(BlendingAttribute.Type);
		blend.opacity = Math.abs(MathUtils.sin(flicker += FLICKER_RATE));
		material.set(blend);
	}

	/**
	 * Moves the cursor's model according to the mouse.
	 *
	 * @param screenX
	 * @param screenY
	 * @param camera
	 * @param map
	 * @return Whether the cursor was moved.
	 */
	@SuppressWarnings("JavaDoc")
	public boolean updateCursorByScreenCoords(final int screenX,
											  final int screenY,
											  final OrthographicCamera camera,
											  final GameMap map) {
		if (highlighter != null) {
			Vector3 collisionPoint = Utils.castRayTowardsPlane(screenX, screenY, camera);
			updateCursorModelAndAdditionalsByCollisionPoint(map, collisionPoint);
			return true;
		}
		return false;
	}

	private void updateCursorModelAndAdditionalsByCollisionPoint(final GameMap map, final Vector3 collisionPoint) {
		int x = MathUtils.clamp((int) collisionPoint.x, 0, map.getNodes()[0].length - 1);
		int z = MathUtils.clamp((int) collisionPoint.z, 0, map.getNodes().length - 1);
		float y = (map.getNodes()[z][x] != null ? map.getNodes()[z][x].getHeight() : 0) + 0.01f;
		highlighter.transform.setTranslation(x, y, z);
		updateCursorAdditionals(x, y, z, MapRendererImpl.getMode());
	}

	/**
	 * Renders the tiles marking when tiling.
	 *
	 * @param srcRow     The row it started.
	 * @param srcCol     The row it started.
	 * @param modelBatch
	 */
	@SuppressWarnings("JavaDoc")
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

	private void updateCursorOfDecalMode(final int x, final float y, final int z, final EditorMode mode) {
		float xFinal = x + 0.5f;
		float yFinal = y + BILLBOARD_Y;
		float zFinal = z + 0.5f;
		if (mode == EditModes.CHARACTERS) {
			Optional.ofNullable(cursorCharacterDecal).ifPresent(c -> c.getDecal().setPosition(xFinal, yFinal, zFinal));
		} else {
			cursorSimpleDecal.setPosition(xFinal, yFinal, zFinal);
		}
	}

	private void updateCursorAdditionals(final int x, final float y, final int z, final EditorMode mode) {
		ModelInstance modelInstance = cursorHandlerModelData.getCursorSelectionModel().getModelInstance();
		if (modelInstance != null) {
			modelInstance.transform.setTranslation(x, y, z);
		}
		updateCursorOfDecalMode(x, y, z, mode);
	}

	/**
	 * Create the certain cursors types.
	 *
	 * @param assetsManager
	 * @param tileModel
	 */
	@SuppressWarnings("JavaDoc")
	public void createCursors(final GameAssetsManager assetsManager, final Model tileModel) {
		cursorHandlerModelData.createCursors(tileModel);
//		createCursorCharacterDecal(assetsManager);
		createCursorSimpleDecal(assetsManager);
	}


	/**
	 * @param selectedElement
	 * @param modelBatch
	 */
	@SuppressWarnings("JavaDoc")
	public void renderModelCursorFloorGrid(final EnvironmentDefinitions selectedElement, final ModelBatch modelBatch) {
		ModelInstance cursorTileModelInstance = cursorHandlerModelData.getCursorTileModelInstance();
		Vector3 originalPosition = cursorTileModelInstance.transform.getTranslation(auxVector3_1);
		Vector3 cursorPosition = highlighter.transform.getTranslation(auxVector3_3);
		cursorPosition.y = CURSOR_Y;
		Direction facingDirection = cursorHandlerModelData.getCursorSelectionModel().getFacingDirection();
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
			ModelInstance cursorTileModelInstance = cursorHandlerModelData.getCursorTileModelInstance();
			cursorTileModelInstance.transform.setTranslation(cursorPosition).translate(col, 0, row);
			modelBatch.render(cursorTileModelInstance);
		}
	}

	private void createCursorCharacterDecal(final GameAssetsManager assetsManager, CharacterDefinition definition) {
		cursorCharacterDecal = Utils.createCharacterDecal(
				assetsManager,
				definition,
				new FlatNode(0, 0),
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
	public void dispose( ) {
	}

	public void initializeCursorCharacterDecal(GameAssetsManager assetsManager, CharacterDefinition definition) {
		createCursorCharacterDecal(assetsManager, definition);
	}
}
