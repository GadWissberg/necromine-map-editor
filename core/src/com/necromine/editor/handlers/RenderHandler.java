package com.necromine.editor.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.ElementDefinition;
import com.gadarts.necromine.model.characters.Direction;
import com.gadarts.necromine.model.env.EnvironmentDefinitions;
import com.gadarts.necromine.model.map.MapNodeData;
import com.gadarts.necromine.model.map.NodeWalls;
import com.gadarts.necromine.model.map.Wall;
import com.necromine.editor.CursorSelectionModel;
import com.necromine.editor.MapRendererImpl;
import com.necromine.editor.actions.processes.MappingProcess;
import com.necromine.editor.handlers.action.ActionsHandler;
import com.necromine.editor.mode.EditModes;
import com.necromine.editor.mode.EditorMode;
import com.necromine.editor.mode.tools.ElementTools;
import com.necromine.editor.model.elements.*;
import com.necromine.editor.model.node.FlatNode;
import com.necromine.editor.utils.Utils;
import lombok.Getter;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class RenderHandler implements Disposable {
	private static final Vector3 auxVector3_1 = new Vector3();
	private static final Matrix4 auxMatrix = new Matrix4();
	private static final Vector2 auxVector2_1 = new Vector2();
	private static final int DECALS_POOL_SIZE = 200;
	private static final Color GRID_COLOR = Color.GRAY;

	private final AxisModelHandler axisModelHandler = new AxisModelHandler();
	private final GameAssetsManager assetsManager;
	private final HandlersManager handlersManager;
	private final Camera camera;
	@Getter
	private final Model tileModel;
	private Model gridModel;
	private ModelInstance gridModelInstance;
	@Getter
	private ModelBatch modelBatch;

	@Getter
	private DecalBatch decalBatch;

	public RenderHandler(final GameAssetsManager assetsManager,
						 final HandlersManager handlersManager,
						 final Camera camera) {
		tileModel = createRectModel();
		this.assetsManager = assetsManager;
		this.handlersManager = handlersManager;
		this.camera = camera;
	}

	public void renderDecal(final Decal decal, final Camera camera) {
		decal.lookAt(auxVector3_1.set(decal.getPosition()).sub(camera.direction), camera.up);
		decalBatch.add(decal);
	}

	void createBatches(final Camera camera) {
		CameraGroupStrategy groupStrategy = new CameraGroupStrategy(camera);
		this.decalBatch = new DecalBatch(DECALS_POOL_SIZE, groupStrategy);
		this.modelBatch = new ModelBatch();
	}

	private Model createRectModel() {
		ModelBuilder builder = new ModelBuilder();
		BlendingAttribute highlightBlend = new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Material material = new Material(highlightBlend);
		return builder.createRect(
				0, 0, 1,
				1, 0, 1,
				1, 0, 0,
				0, 0, 0,
				0, 1, 0,
				material,
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates
		);
	}

	public void draw(final EditorMode mode, final PlacedElements placedElements, final Set<MapNodeData> initializedTiles, final ElementDefinition selectedElement) {
		int sam = Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0;
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | sam);
		renderModels(initializedTiles, placedElements, mode, selectedElement);
		renderDecals(handlersManager, mode, placedElements);
	}

	public void renderAux(final ModelBatch modelBatch) {
		axisModelHandler.render(modelBatch);
		modelBatch.render(gridModelInstance);
	}

	public void createModels(final Dimension levelSize) {
		Model axisModelX = axisModelHandler.getAxisModelX();
		Model axisModelY = axisModelHandler.getAxisModelY();
		Model axisModelZ = axisModelHandler.getAxisModelZ();
		if (axisModelX == null && axisModelY == null && axisModelZ == null) {
			axisModelHandler.createAxis();
		}
		createGrid(levelSize);
	}

	private void createGrid(final Dimension levelSize) {
		Gdx.app.postRunnable(() -> {
			if (gridModel != null) {
				gridModel.dispose();
			}
			ModelBuilder builder = new ModelBuilder();
			Material material = new Material(ColorAttribute.createDiffuse(GRID_COLOR));
			int attributes = VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal;
			gridModel = builder.createLineGrid(levelSize.width, levelSize.height, 1, 1, material, attributes);
			gridModelInstance = new ModelInstance(gridModel);
			gridModelInstance.transform.translate(levelSize.width / 2f, 0.01f, levelSize.height / 2f);
		});
	}

	private void renderDecals(final HandlersManager handlersManager, final EditorMode mode, final PlacedElements placedElements) {
		Gdx.gl.glDepthMask(false);
		if (handlersManager.getLogicHandlers().getCursorHandler().getHighlighter() != null && mode.getClass().equals(EditModes.class) && ((EditModes) mode).isDecalCursor()) {
			renderCursorOfDecalMode(handlersManager, mode, camera);
		}
		renderDecalPlacedElements(placedElements, handlersManager, camera);
		handlersManager.getRenderHandler().getDecalBatch().flush();
		Gdx.gl.glDepthMask(true);
	}

	private void renderDecalPlacedElements(final PlacedElements placedElements,
										   final HandlersManager handlersManager,
										   final Camera camera) {
		Map<EditModes, List<? extends PlacedElement>> placedObjects = placedElements.getPlacedObjects();
		List<PlacedCharacter> placedCharacters = (List<PlacedCharacter>) placedObjects.get(EditModes.CHARACTERS);
		for (final PlacedCharacter character : placedCharacters) {
			renderCharacter(character.getCharacterDecal(), character.getCharacterDecal().getSpriteDirection(), camera, handlersManager);
		}
		List<PlacedLight> placedLights = (List<PlacedLight>) placedObjects.get(EditModes.LIGHTS);
		for (final PlacedLight placedLight : placedLights) {
			handlersManager.getRenderHandler().renderDecal(placedLight.getDecal(), camera);
		}
	}


	private void renderCursorOfDecalMode(final HandlersManager handlersManager,
										 final EditorMode mode,
										 final Camera camera) {
		CursorHandler cursorHandler = handlersManager.getLogicHandlers().getCursorHandler();
		if (mode == EditModes.CHARACTERS) {
			CharacterDecal cursorCharacterDecal = cursorHandler.getCursorCharacterDecal();
			renderCharacter(cursorCharacterDecal, cursorCharacterDecal.getSpriteDirection(), camera, handlersManager);
		} else {
			handlersManager.getRenderHandler().renderDecal(cursorHandler.getCursorSimpleDecal(), camera);
		}
	}

	private void renderCharacter(final CharacterDecal characterDecal,
								 final Direction facingDirection,
								 final Camera camera,
								 final HandlersManager handlersManager) {
		Utils.applyFrameSeenFromCameraForCharacterDecal(characterDecal, facingDirection, camera, assetsManager);
		handlersManager.getRenderHandler().renderDecal(characterDecal.getDecal(), camera);
	}


	private void renderModels(final Set<MapNodeData> initializedTiles,
							  final PlacedElements placedElements,
							  final EditorMode mode,
							  final ElementDefinition selectedElement) {
		ModelBatch modelBatch = handlersManager.getRenderHandler().getModelBatch();
		modelBatch.begin(camera);
		renderAux(modelBatch);
		renderCursor(mode, selectedElement);
		renderExistingProcess();
		renderModelPlacedElements(initializedTiles, placedElements);
		modelBatch.end();
	}

	private void renderModelPlacedElements(final Set<MapNodeData> initializedTiles, final PlacedElements placedElements) {
		renderTiles(initializedTiles);
		renderEnvObjects(placedElements);
		List<PlacedPickup> placedPickups = (List<PlacedPickup>) placedElements.getPlacedObjects().get(EditModes.PICKUPS);
		for (final PlacedPickup pickup : placedPickups) {
			renderPickup(pickup.getModelInstance());
		}
	}

	private void renderTiles(final Set<MapNodeData> initializedTiles) {
		for (final MapNodeData tile : initializedTiles) {
			if (tile.getModelInstance() != null) {
				ModelBatch modelBatch = handlersManager.getRenderHandler().getModelBatch();
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
		ModelInstance highlighter = handlersManager.getLogicHandlers().getCursorHandler().getHighlighter();
		if (highlighter == null) return;
		if (MapRendererImpl.getTool() != ElementTools.BRUSH) {
			handlersManager.getRenderHandler().getModelBatch().render(highlighter);
		}
		renderCursorObjectModel(selectedElement, mode);
	}

	private void renderCursorObjectModel(final ElementDefinition selectedElement, final EditorMode mode) {
		CursorHandler cursorHandler = handlersManager.getLogicHandlers().getCursorHandler();
		if (selectedElement != null) {
			if (mode == EditModes.ENVIRONMENT) {
				EnvironmentDefinitions environmentDefinition = (EnvironmentDefinitions) selectedElement;
				cursorHandler.renderModelCursorFloorGrid(environmentDefinition, handlersManager.getRenderHandler().getModelBatch());
				CursorHandlerModelData cursorHandlerModelData = cursorHandler.getCursorHandlerModelData();
				CursorSelectionModel cursorSelectionModel = cursorHandlerModelData.getCursorSelectionModel();
				ModelInstance modelInstance = cursorSelectionModel.getModelInstance();
				renderEnvObject(environmentDefinition, modelInstance, cursorSelectionModel.getFacingDirection());
			} else if (mode == EditModes.PICKUPS) {
				renderPickup(cursorHandler.getCursorHandlerModelData().getCursorSelectionModel().getModelInstance());
			}
		}
	}

	private void renderEnvObjects(final PlacedElements placedElements) {
		List<PlacedEnvObject> placedEnvObjects = (List<PlacedEnvObject>) placedElements.getPlacedObjects().get(EditModes.ENVIRONMENT);
		for (final PlacedEnvObject placedEnvObject : placedEnvObjects) {
			renderEnvObject(
					(EnvironmentDefinitions) placedEnvObject.getDefinition(),
					placedEnvObject.getModelInstance(),
					placedEnvObject.getFacingDirection());
		}
	}

	private void renderPickup(final ModelInstance modelInstance) {
		Matrix4 originalTransform = auxMatrix.set(modelInstance.transform);
		modelInstance.transform.translate(0.5f, 0, 0.5f);
		handlersManager.getRenderHandler().getModelBatch().render(modelInstance);
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
		handlersManager.getRenderHandler().getModelBatch().render(modelInstance);
		modelInstance.transform.set(originalTransform);
	}


	private void renderExistingProcess() {
		ActionsHandler actionsHandler = handlersManager.getLogicHandlers().getActionsHandler();
		MappingProcess<? extends MappingProcess.FinishProcessParameters> p = actionsHandler.getCurrentProcess();
		Optional.ofNullable(p).ifPresent(process -> {
			if (p.isRequiresRegionSelectionCursor()) {
				FlatNode srcNode = p.getSrcNode();
				handlersManager.getLogicHandlers().getCursorHandler().renderRectangleMarking(
						srcNode.getRow(),
						srcNode.getCol(),
						handlersManager.getRenderHandler().getModelBatch());
			}
		});
	}

	public void render(final EditorMode mode,
					   final PlacedElements placedElements,
					   final ElementDefinition selectedElement) {
		draw(mode, placedElements, placedElements.getPlacedTiles(), selectedElement);
	}

	@Override
	public void dispose() {
		tileModel.dispose();
		modelBatch.dispose();
		decalBatch.dispose();
		axisModelHandler.dispose();
		gridModel.dispose();
	}

}
