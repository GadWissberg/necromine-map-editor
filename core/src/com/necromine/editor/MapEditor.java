package com.necromine.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gadarts.necromine.WallCreator;
import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.ElementDefinition;
import com.gadarts.necromine.model.EnvironmentDefinitions;
import com.gadarts.necromine.model.GeneralUtils;
import com.gadarts.necromine.model.MapNodeData;
import com.gadarts.necromine.model.characters.CharacterDefinition;
import com.gadarts.necromine.model.characters.Direction;
import com.gadarts.necromine.model.pickups.ItemDefinition;
import com.necromine.editor.handlers.CursorHandler;
import com.necromine.editor.handlers.HandlersManagerImpl;
import com.necromine.editor.handlers.ResourcesHandler;
import com.necromine.editor.mode.CameraModes;
import com.necromine.editor.mode.EditModes;
import com.necromine.editor.mode.EditorMode;
import com.necromine.editor.mode.tools.EditorTool;
import com.necromine.editor.mode.tools.TilesTools;
import com.necromine.editor.model.elements.CharacterDecal;
import com.necromine.editor.model.elements.PlacedElements;
import com.necromine.editor.model.elements.PlacedEnvObject;
import com.necromine.editor.model.node.FlatNode;
import com.necromine.editor.model.node.NodeWallsDefinitions;
import lombok.Getter;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

/**
 * The world renderer.
 */
public class MapEditor extends Editor implements GuiEventsSubscriber {

	/**
	 * Camera's far.
	 */
	public static final float FAR = 200f;

	/**
	 * The rate of the cursor flicker animation.
	 */
	public static final Vector3 auxVector3_1 = new Vector3();
	public static final int TARGET_VERSION = 5;
	private static final float NEAR = 0.01f;
	private static final float CAMERA_HEIGHT = 14;
	private static final float[] CAMERA_START_POINT = {12F, CAMERA_HEIGHT, 12F};
	private static final float[] CAMERA_INITIAL_POSITION = {4F, 6F, 4F};

	@Getter
	private static EditorMode mode = EditModes.TILES;

	@Getter
	private static EditorTool tool = TilesTools.BRUSH;

	public final int VIEWPORT_WIDTH;
	public final int VIEWPORT_HEIGHT;
	private final PlacedElements placedElements = new PlacedElements();
	private final Vector2 lastMouseTouchPosition = new Vector2();
	private final HandlersManagerImpl handlers;
	private final MapManagerEventsNotifier eventsNotifier = new MapManagerEventsNotifier();
	private final GameMap map = new GameMap();
	private WallCreator wallCreator;
	private OrthographicCamera camera;
	private Assets.SurfaceTextures selectedTile;
	private ElementDefinition selectedElement;

	public MapEditor(final int width, final int height, final String assetsLocation) {
		VIEWPORT_WIDTH = width / 50;
		VIEWPORT_HEIGHT = height / 50;
		handlers = new HandlersManagerImpl(map, eventsNotifier, placedElements);
		ResourcesHandler resourcesHandler = handlers.getResourcesHandler();
		resourcesHandler.init(assetsLocation);
		CursorHandler cursorHandler = handlers.getCursorHandler();
		GameAssetsManager assetsManager = resourcesHandler.getAssetsManager();
		cursorHandler.setCursorSelectionModel(new CursorSelectionModel(assetsManager));
		handlers.getMapFileHandler().init(assetsManager, cursorHandler, placedElements.getPlacedTiles());
		Arrays.stream(EditModes.values()).forEach(mode -> placedElements.getPlacedObjects().put(mode, new ArrayList<>()));
	}


	@Override
	public void create( ) {
		GameAssetsManager assetsManager = handlers.getResourcesHandler().getAssetsManager();
		wallCreator = new WallCreator(assetsManager);
		camera = createCamera();
		handlers.onCreate(camera, wallCreator, map.getDimension());
		initializeInput();
	}


	private OrthographicCamera createCamera( ) {
		OrthographicCamera cam = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
		cam.near = NEAR;
		cam.far = FAR;
		cam.update();
		initializeCameraPosition(cam);
		return cam;
	}

	private void initializeCameraPosition(final OrthographicCamera cam) {
		cam.position.set(CAMERA_INITIAL_POSITION);
		cam.up.set(0, 1, 0);
		cam.zoom = 1;
		cam.lookAt(auxVector3_1.setZero());
		cam.position.set(CAMERA_START_POINT);
	}

	@Override
	public void render( ) {
		update();
		handlers.getRenderHandler().render(mode, placedElements, selectedElement);
	}


	private void update( ) {
		InputProcessor inputProcessor = Gdx.input.getInputProcessor();
		if (inputProcessor != null && DefaultSettings.ENABLE_DEBUG_INPUT) {
			CameraInputController cameraInputController = (CameraInputController) inputProcessor;
			cameraInputController.update();
		}
		camera.update();
		CursorHandler cursorHandler = handlers.getCursorHandler();
		if (cursorHandler.getHighlighter() != null) {
			cursorHandler.updateCursorFlicker(mode);
		}
	}


	@Override
	public void dispose( ) {
		handlers.dispose();
		wallCreator.dispose();
	}

	@Override
	public void onTileSelected(final Assets.SurfaceTextures texture) {
		selectedTile = texture;
		handlers.onTileSelected();
	}

	@Override
	public void onEditModeSet(final EditModes mode) {
		onModeSet(mode);
		if (mode != EditModes.LIGHTS && mode != EditModes.PICKUPS) {
			if (mode != EditModes.TILES || selectedTile == null) {
				handlers.getCursorHandler().setHighlighter(null);
			} else {
				onTileSelected(selectedTile);
			}
		} else {
			handlers.getCursorHandler().setHighlighter(handlers.getCursorHandler().getCursorTileModelInstance());
		}
	}

	private void onModeSet(final EditorMode mode) {
		selectedElement = null;
		handlers.getCursorHandler().setHighlighter(null);
		MapEditor.mode = mode;
	}

	@Override
	public void onTreeCharacterSelected(final CharacterDefinition definition) {
		selectedElement = definition;
		handlers.onTreeCharacterSelected(selectedElement, definition);
	}

	@Override
	public void onSelectedObjectRotate(final int direction) {
		if (selectedElement != null) {
			CursorHandler cursorHandler = handlers.getCursorHandler();
			if (mode == EditModes.CHARACTERS) {
				CharacterDecal cursorCharacterDecal = cursorHandler.getCursorCharacterDecal();
				int ordinal = cursorCharacterDecal.getSpriteDirection().ordinal() + direction;
				int length = Direction.values().length;
				int index = (ordinal < 0 ? ordinal + length : ordinal) % length;
				cursorCharacterDecal.setSpriteDirection(Direction.values()[index]);
			} else {
				CursorSelectionModel cursorSelectionModel = cursorHandler.getCursorSelectionModel();
				int ordinal = cursorSelectionModel.getFacingDirection().ordinal() + direction * 2;
				int length = Direction.values().length;
				int index = (ordinal < 0 ? ordinal + length : ordinal) % length;
				cursorSelectionModel.setFacingDirection(Direction.values()[index]);
			}
		}
	}

	@Override
	public void onTreeEnvSelected(final EnvironmentDefinitions env) {
		selectedElement = env;
		handlers.onTreeEnvSelected(selectedElement);
	}

	@Override
	public void onTreePickupSelected(final ItemDefinition definition) {
		selectedElement = definition;
		handlers.onTreePickupSelected(selectedElement, definition);
	}

	@Override
	public void onCameraModeSet(final CameraModes mode) {
		onModeSet(mode);
	}

	@Override
	public void onSaveMapRequested( ) {
		handlers.getMapFileHandler().onSaveMapRequested(map, placedElements);
	}

	@Override
	public void onNewMapRequested( ) {
		placedElements.clear();
		map.reset();
		initializeCameraPosition(camera);
		handlers.getViewAuxHandler().createModels(map.getDimension());
	}

	@Override
	public void onLoadMapRequested( ) {
		handlers.getMapFileHandler().onLoadMapRequested(map, placedElements, wallCreator, handlers.getViewAuxHandler());
	}

	@Override
	public void onToolSet(final EditorTool tool) {
		selectedElement = null;
		CursorHandler cursorHandler = handlers.getCursorHandler();
		if (tool != TilesTools.BRUSH) {
			cursorHandler.setHighlighter(cursorHandler.getCursorTileModelInstance());
		} else {
			if (selectedTile == null) {
				cursorHandler.setHighlighter(null);
			}
		}
		MapEditor.tool = tool;
	}

	@Override
	public void onNodeWallsDefined(final NodeWallsDefinitions definitions, final FlatNode src, final FlatNode dst) {
		handlers.getActionsHandler().onNodeWallsDefined(definitions, src, dst);
	}

	@Override
	public void onTilesLift(final FlatNode src, final FlatNode dst, final float value) {
		handlers.getActionsHandler().onTilesLift(src, dst, value);
	}

	@Override
	public float getAmbientLightValue( ) {
		return map.getAmbientLight();
	}

	@Override
	public void onAmbientLightValueSet(final float value) {
		map.setAmbientLight(value);
	}

	@Override
	public void onEnvObjectDefined(final PlacedEnvObject element, final float height) {
		handlers.getActionsHandler().onEnvObjectDefined(element, height);
	}

	@Override
	public void onMapSizeSet(final int width, final int depth) {
		if (this.map.getNodes().length == depth && this.map.getNodes()[0].length == width) return;
		Dimension dimension = new Dimension(width, depth);
		map.resetSize(dimension);
		handlers.getViewAuxHandler().createModels(dimension);
	}

	@Override
	public Dimension getMapSize( ) {
		MapNodeData[][] nodes = map.getNodes();
		return new Dimension(nodes.length, nodes[0].length);
	}


	void initializeInput( ) {
		if (DefaultSettings.ENABLE_DEBUG_INPUT) {
			CameraInputController processor = new CameraInputController(camera);
			Gdx.input.setInputProcessor(processor);
			processor.autoUpdate = true;
		} else {
			Gdx.input.setInputProcessor(this);
		}
	}

	@Override
	public boolean touchDown(final int screenX, final int screenY, final int pointer, final int button) {
		if (button == Input.Buttons.LEFT) {
			lastMouseTouchPosition.set(screenX, screenY);
		}
		Set<MapNodeData> placedTiles = placedElements.getPlacedTiles();
		GameAssetsManager assetsManager = handlers.getResourcesHandler().getAssetsManager();
		return handlers.getActionsHandler().onTouchDown(assetsManager, placedTiles, button);
	}

	@Override
	public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button) {
		return handlers.getActionsHandler().onTouchUp(selectedTile, handlers.getCursorHandler().getCursorTileModel());
	}

	@Override
	public boolean touchDragged(final int screenX, final int screenY, final int pointer) {
		boolean result = true;
		if (mode.getClass().equals(CameraModes.class)) {
			Vector3 rotationPoint = GeneralUtils.defineRotationPoint(auxVector3_1, camera);
			((CameraModes) mode).getManipulation().run(lastMouseTouchPosition, camera, screenX, screenY, rotationPoint);
		} else {
			result = handlers.getCursorHandler().updateCursorByScreenCoords(screenX, screenY, camera, map);
		}
		lastMouseTouchPosition.set(screenX, screenY);
		return result;
	}

	@Override
	public boolean mouseMoved(final int screenX, final int screenY) {
		return handlers.getCursorHandler().updateCursorByScreenCoords(screenX, screenY, camera, map);
	}

	public void subscribeForEvents(final MapManagerEventsSubscriber subscriber) {
		eventsNotifier.subscribeForEvents(subscriber);
	}
}
