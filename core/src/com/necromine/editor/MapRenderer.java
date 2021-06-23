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
import com.gadarts.necromine.model.*;
import com.gadarts.necromine.model.characters.Direction;
import com.necromine.editor.actions.processes.MappingProcess;
import com.necromine.editor.handlers.CursorHandler;
import com.necromine.editor.handlers.HandlersManager;
import com.necromine.editor.handlers.action.ActionsHandler;
import com.necromine.editor.mode.EditModes;
import com.necromine.editor.mode.EditorMode;
import com.necromine.editor.mode.tools.EnvTools;
import com.necromine.editor.model.elements.*;
import com.necromine.editor.model.node.FlatNode;
import com.necromine.editor.utils.Utils;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
public class MapRenderer {
	private static final Vector3 auxVector3_1 = new Vector3();
	private static final Matrix4 auxMatrix = new Matrix4();
	private static final Vector2 auxVector2_1 = new Vector2();
	private final GameAssetsManager assetsManager;
	private final HandlersManager handlersManager;
	private final OrthographicCamera camera;

	public void draw(final EditorMode mode, final PlacedElements placedElements, final Set<MapNodeData> initializedTiles, final ElementDefinition selectedElement) {
		int sam = Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0;
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | sam);
		renderModels(initializedTiles, placedElements, mode, selectedElement);
		renderDecals(handlersManager, mode, placedElements);
	}

	private void renderDecals(final HandlersManager handlersManager, final EditorMode mode, final PlacedElements placedElements) {
		Gdx.gl.glDepthMask(false);
		if (handlersManager.getCursorHandler().getHighlighter() != null && mode.getClass().equals(EditModes.class) && ((EditModes) mode).isDecalCursor()) {
			renderCursorOfDecalMode(handlersManager, mode, camera);
		}
		renderDecalPlacedElements(placedElements, handlersManager, camera);
		handlersManager.getBatchHandler().getDecalBatch().flush();
		Gdx.gl.glDepthMask(true);
	}

	private void renderDecalPlacedElements(final PlacedElements placedElements, final HandlersManager handlersManager, final OrthographicCamera camera) {
		Map<EditModes, List<? extends PlacedElement>> placedObjects = placedElements.getPlacedObjects();
		List<PlacedCharacter> placedCharacters = (List<PlacedCharacter>) placedObjects.get(EditModes.CHARACTERS);
		for (PlacedCharacter character : placedCharacters) {
			renderCharacter(character.getCharacterDecal(), character.getCharacterDecal().getSpriteDirection(), camera, handlersManager);
		}
		List<PlacedLight> placedLights = (List<PlacedLight>) placedObjects.get(EditModes.LIGHTS);
		for (PlacedLight placedLight : placedLights) {
			handlersManager.getBatchHandler().renderDecal(placedLight.getDecal(), camera);
		}
	}


	private void renderCursorOfDecalMode(final HandlersManager handlersManager, final EditorMode mode, final OrthographicCamera camera) {
		CursorHandler cursorHandler = handlersManager.getCursorHandler();
		if (mode == EditModes.CHARACTERS) {
			CharacterDecal cursorCharacterDecal = cursorHandler.getCursorCharacterDecal();
			renderCharacter(cursorCharacterDecal, cursorCharacterDecal.getSpriteDirection(), camera, handlersManager);
		} else {
			handlersManager.getBatchHandler().renderDecal(cursorHandler.getCursorSimpleDecal(), camera);
		}
	}

	private void renderCharacter(final CharacterDecal characterDecal, final Direction facingDirection, final OrthographicCamera camera, final HandlersManager handlersManager) {
		Utils.applyFrameSeenFromCameraForCharacterDecal(characterDecal, facingDirection, camera, assetsManager);
		handlersManager.getBatchHandler().renderDecal(characterDecal.getDecal(), camera);
	}


	private void renderModels(final Set<MapNodeData> initializedTiles,
							  final PlacedElements placedElements,
							  final EditorMode mode,
							  final ElementDefinition selectedElement) {
		ModelBatch modelBatch = handlersManager.getBatchHandler().getModelBatch();
		modelBatch.begin(camera);
		handlersManager.getViewAuxHandler().renderAux(modelBatch);
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
				ModelBatch modelBatch = handlersManager.getBatchHandler().getModelBatch();
				modelBatch.render(tile.getModelInstance());
				NodeWalls walls = tile.getWalls();
				renderWall(modelBatch, walls.getNorthWall());
				renderWall(modelBatch, walls.getEastWall());
				renderWall(modelBatch, walls.getWestWall());
				renderWall(modelBatch, walls.getSouthWall());
			}
		}
	}

	private void renderWall(final ModelBatch modelBatch, final Wall wall) {
		if (wall != null) {
			modelBatch.render(wall.getModelInstance());
		}
	}

	public void renderCursor(final EditorMode mode, final ElementDefinition selectedElement) {
		ModelInstance highlighter = handlersManager.getCursorHandler().getHighlighter();
		if (highlighter == null) return;
		if (MapEditor.getTool() != EnvTools.BRUSH) {
			handlersManager.getBatchHandler().getModelBatch().render(highlighter);
		}
		renderCursorObjectModel(selectedElement, mode);
	}

	private void renderCursorObjectModel(final ElementDefinition selectedElement, final EditorMode mode) {
		CursorHandler cursorHandler = handlersManager.getCursorHandler();
		if (selectedElement != null) {
			if (mode == EditModes.ENVIRONMENT) {
				EnvironmentDefinitions environmentDefinition = (EnvironmentDefinitions) selectedElement;
				cursorHandler.renderModelCursorFloorGrid(environmentDefinition, handlersManager.getBatchHandler().getModelBatch());
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
		handlersManager.getBatchHandler().getModelBatch().render(modelInstance);
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
		handlersManager.getBatchHandler().getModelBatch().render(modelInstance);
		modelInstance.transform.set(originalTransform);
	}


	private void renderExistingProcess() {
		ActionsHandler actionsHandler = handlersManager.getActionsHandler();
		MappingProcess<? extends MappingProcess.FinishProcessParameters> p = actionsHandler.getCurrentProcess();
		Optional.ofNullable(p).ifPresent(process -> {
			if (p.isRequiresRegionSelectionCursor()) {
				FlatNode srcNode = p.getSrcNode();
				handlersManager.getCursorHandler().renderRectangleMarking(
						srcNode.getRow(),
						srcNode.getCol(),
						handlersManager.getBatchHandler().getModelBatch());
			}
		});
	}

}
