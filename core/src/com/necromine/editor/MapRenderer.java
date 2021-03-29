package com.necromine.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.ElementDefinition;
import com.gadarts.necromine.model.EnvironmentDefinitions;
import com.gadarts.necromine.model.MapNodeData;
import com.gadarts.necromine.model.Wall;
import com.gadarts.necromine.model.characters.Direction;
import com.necromine.editor.actions.processes.MappingProcess;
import com.necromine.editor.actions.processes.PlaceTilesProcess;
import com.necromine.editor.actions.processes.SelectTilesForLiftProcess;
import com.necromine.editor.handlers.CursorHandler;
import com.necromine.editor.handlers.Handlers;
import com.necromine.editor.handlers.action.ActionsHandler;
import com.necromine.editor.mode.EditModes;
import com.necromine.editor.mode.EditorMode;
import com.necromine.editor.model.elements.CharacterDecal;
import com.necromine.editor.model.elements.PlacedCharacter;
import com.necromine.editor.model.elements.PlacedElement;
import com.necromine.editor.model.elements.PlacedElements;
import com.necromine.editor.model.elements.PlacedEnvObject;
import com.necromine.editor.model.elements.PlacedLight;
import com.necromine.editor.model.elements.PlacedPickup;
import com.necromine.editor.model.node.Node;
import com.necromine.editor.utils.Utils;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
public class MapRenderer {
	private static final Vector3 auxVector3_1 = new Vector3();
	private static final Matrix4 auxMatrix = new Matrix4();
	private static final Vector2 auxVector2_1 = new Vector2();
	private final GameAssetsManager assetsManager;
	private final Handlers handlers;
	private final OrthographicCamera camera;
	private final PlacedElements placedElements;

	public void draw(final EditorMode mode, final PlacedElements placedElements, final Set<MapNodeData> initializedTiles, final ElementDefinition selectedElement) {
		int sam = Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0;
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | sam);
		renderModels(initializedTiles, placedElements, mode, selectedElement);
		renderDecals(handlers, mode, placedElements);
	}

	private void renderDecals(final Handlers handlers, final EditorMode mode, final PlacedElements placedElements) {
		Gdx.gl.glDepthMask(false);
		if (handlers.getCursorHandler().getHighlighter() != null && mode.getClass().equals(EditModes.class) && ((EditModes) mode).isDecalCursor()) {
			renderCursorOfDecalMode(handlers, mode, camera);
		}
		renderDecalPlacedElements(placedElements, handlers, camera);
		handlers.getBatchHandler().getDecalBatch().flush();
		Gdx.gl.glDepthMask(true);
	}

	private void renderDecalPlacedElements(final PlacedElements placedElements, final Handlers handlers, final OrthographicCamera camera) {
		Map<EditModes, List<? extends PlacedElement>> placedObjects = placedElements.getPlacedObjects();
		List<PlacedCharacter> placedCharacters = (List<PlacedCharacter>) placedObjects.get(EditModes.CHARACTERS);
		for (PlacedCharacter character : placedCharacters) {
			renderCharacter(character.getCharacterDecal(), character.getCharacterDecal().getSpriteDirection(), camera, handlers);
		}
		List<PlacedLight> placedLights = (List<PlacedLight>) placedObjects.get(EditModes.LIGHTS);
		for (PlacedLight placedLight : placedLights) {
			handlers.getBatchHandler().renderDecal(placedLight.getDecal(), camera);
		}
	}


	private void renderCursorOfDecalMode(final Handlers handlers, final EditorMode mode, final OrthographicCamera camera) {
		CursorHandler cursorHandler = handlers.getCursorHandler();
		if (mode == EditModes.CHARACTERS) {
			CharacterDecal cursorCharacterDecal = cursorHandler.getCursorCharacterDecal();
			renderCharacter(cursorCharacterDecal, cursorCharacterDecal.getSpriteDirection(), camera, handlers);
		} else {
			handlers.getBatchHandler().renderDecal(cursorHandler.getCursorSimpleDecal(), camera);
		}
	}

	private void renderCharacter(final CharacterDecal characterDecal, final Direction facingDirection, final OrthographicCamera camera, final Handlers handlers) {
		Utils.applyFrameSeenFromCameraForCharacterDecal(characterDecal, facingDirection, camera, assetsManager);
		handlers.getBatchHandler().renderDecal(characterDecal.getDecal(), camera);
	}


	private void renderModels(final Set<MapNodeData> initializedTiles, final PlacedElements placedElements, final EditorMode mode, final ElementDefinition selectedElement) {
		ModelBatch modelBatch = handlers.getBatchHandler().getModelBatch();
		modelBatch.begin(camera);
		handlers.getViewAuxHandler().renderAux(modelBatch);
		renderCursor(mode, selectedElement);
		renderExistingProcess();
		renderModelPlacedElements(initializedTiles, placedElements);
		modelBatch.end();
	}

	private void renderModelPlacedElements(final Set<MapNodeData> initializedTiles, final PlacedElements placedElements) {
		renderTiles(initializedTiles);
		renderEnvObjects(placedElements);
		List<PlacedPickup> placedPickups = (List<PlacedPickup>) placedElements.getPlacedObjects().get(EditModes.PICKUPS);
		for (PlacedPickup pickup : placedPickups) {
			renderPickup(pickup.getModelInstance());
		}
	}

	private void renderTiles(final Set<MapNodeData> initializedTiles) {
		for (MapNodeData tile : initializedTiles) {
			if (tile.getModelInstance() != null) {
				ModelBatch modelBatch = handlers.getBatchHandler().getModelBatch();
				modelBatch.render(tile.getModelInstance());
				renderWall(modelBatch, tile.getNorthWall());
				renderWall(modelBatch, tile.getEastWall());
				renderWall(modelBatch, tile.getWestWall());
				renderWall(modelBatch, tile.getSouthWall());
			}
		}
	}

	private void renderWall(final ModelBatch modelBatch, final Wall wall) {
		if (wall != null) {
			modelBatch.render(wall.getModelInstance());
		}
	}

	public void renderCursor(final EditorMode mode, final ElementDefinition selectedElement) {
		ModelInstance highlighter = handlers.getCursorHandler().getHighlighter();
		if (highlighter == null) return;
		if (mode != EditModes.ENVIRONMENT) {
			handlers.getBatchHandler().getModelBatch().render(highlighter);
		}
		renderCursorObjectModel(selectedElement, mode);
	}

	private void renderCursorObjectModel(final ElementDefinition selectedElement, final EditorMode mode) {
		CursorHandler cursorHandler = handlers.getCursorHandler();
		if (selectedElement != null) {
			if (mode == EditModes.ENVIRONMENT) {
				EnvironmentDefinitions environmentDefinition = (EnvironmentDefinitions) selectedElement;
				cursorHandler.renderModelCursorFloorGrid(environmentDefinition, handlers.getBatchHandler().getModelBatch());
				CursorSelectionModel cursorSelectionModel = cursorHandler.getCursorSelectionModel();
				ModelInstance modelInstance = cursorSelectionModel.getModelInstance();
				renderEnvObject(environmentDefinition, modelInstance, cursorSelectionModel.getFacingDirection());
			} else if (mode == EditModes.PICKUPS) {
				renderPickup(cursorHandler.getCursorSelectionModel().getModelInstance());
			}
		}
	}

	private void renderEnvObjects(final PlacedElements placedElements) {
		List<PlacedEnvObject> placedEnvObjects = (List<PlacedEnvObject>) placedElements.getPlacedObjects().get(EditModes.ENVIRONMENT);
		for (PlacedEnvObject placedEnvObject : placedEnvObjects) {
			renderEnvObject(
					(EnvironmentDefinitions) placedEnvObject.getDefinition(),
					placedEnvObject.getModelInstance(),
					placedEnvObject.getFacingDirection());
		}
	}

	private void renderPickup(final ModelInstance modelInstance) {
		Matrix4 originalTransform = auxMatrix.set(modelInstance.transform);
		modelInstance.transform.translate(0.5f, 0, 0.5f);
		handlers.getBatchHandler().getModelBatch().render(modelInstance);
		modelInstance.transform.set(originalTransform);
	}

	private void renderEnvObject(final EnvironmentDefinitions definition,
								 final ModelInstance modelInstance,
								 final Direction facingDirection) {
		Matrix4 originalTransform = auxMatrix.set(modelInstance.transform);
		modelInstance.transform.translate(0.5f, 0, 0.5f);
		modelInstance.transform.rotate(Vector3.Y, -1 * facingDirection.getDirection(auxVector2_1).angleDeg());
		modelInstance.transform.translate(definition.getOffset(auxVector3_1));
		EnvironmentDefinitions.handleEvenSize(definition, modelInstance, facingDirection);
		handlers.getBatchHandler().getModelBatch().render(modelInstance);
		modelInstance.transform.set(originalTransform);
	}


	private void renderExistingProcess() {
		ActionsHandler actionsHandler = handlers.getActionsHandler();
		MappingProcess<? extends MappingProcess.FinishProcessParameters> p = actionsHandler.getCurrentProcess();
		if (p instanceof PlaceTilesProcess || p instanceof SelectTilesForLiftProcess) {
			Node srcNode = p.getSrcNode();
			handlers.getCursorHandler().renderRectangleMarking(
					srcNode.getRow(),
					srcNode.getCol(),
					handlers.getBatchHandler().getModelBatch());
		}
	}

}
