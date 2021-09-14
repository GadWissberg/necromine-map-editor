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
import com.gadarts.necromine.model.EnvironmentDefinitions;
import com.gadarts.necromine.model.GeneralUtils;
import com.gadarts.necromine.model.MapNodeData;
import com.gadarts.necromine.model.characters.CharacterDefinition;
import com.gadarts.necromine.model.pickups.ItemDefinition;
import com.necromine.editor.handlers.CursorHandler;
import com.necromine.editor.handlers.HandlersManager;
import com.necromine.editor.handlers.HandlersManagerImpl;
import com.necromine.editor.handlers.ResourcesHandler;
import com.necromine.editor.mode.CameraModes;
import com.necromine.editor.mode.EditModes;
import com.necromine.editor.mode.EditorMode;
import com.necromine.editor.mode.tools.EditorTool;
import com.necromine.editor.mode.tools.TilesTools;
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
	public static EditorMode mode = EditModes.TILES;

	@Getter
	public static EditorTool tool = TilesTools.BRUSH;

	private final MapEditorData data;
	private final Vector2 lastMouseTouchPosition = new Vector2();
	private final HandlersManager handlers;
	private WallCreator wallCreator;
	private OrthographicCamera camera;

	public MapEditor(final int width, final int height, final String assetsLocation) {
		data = new MapEditorData(new ViewportResolution(width / 50, height / 50));
		handlers = new HandlersManagerImpl(data);
		ResourcesHandler resourcesHandler = handlers.getResourcesHandler();
		resourcesHandler.init(assetsLocation);
		CursorHandler cursorHandler = handlers.getLogicHandlers().getCursorHandler();
		GameAssetsManager assetsManager = resourcesHandler.getAssetsManager();
		cursorHandler.setCursorSelectionModel(new CursorSelectionModel(assetsManager));
		PlacedElements placedElements = data.getPlacedElements();
		handlers.getMapFileHandler().init(assetsManager, cursorHandler, placedElements.getPlacedTiles());
		Arrays.stream(EditModes.values()).forEach(mode -> placedElements.getPlacedObjects().put(mode, new ArrayList<>()));
	}


	@Override
	public void create( ) {
		GameAssetsManager assetsManager = handlers.getResourcesHandler().getAssetsManager();
		wallCreator = new WallCreator(assetsManager);
		camera = createCamera();
		handlers.onCreate(camera, wallCreator, data.getMap().getDimension());
		initializeInput();
	}


	private OrthographicCamera createCamera( ) {
		ViewportResolution viewportResolution = data.getViewportResolution();
		OrthographicCamera cam = new OrthographicCamera(
				viewportResolution.VIEWPORT_WIDTH,
				viewportResolution.VIEWPORT_HEIGHT);
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
		PlacedElements placedElements = data.getPlacedElements();
		handlers.getRenderHandler().render(mode, placedElements, handlers.getLogicHandlers().getSelectionHandler().getSelectedElement());
	}


	private void update( ) {
		InputProcessor inputProcessor = Gdx.input.getInputProcessor();
		if (inputProcessor != null && DefaultSettings.ENABLE_DEBUG_INPUT) {
			CameraInputController cameraInputController = (CameraInputController) inputProcessor;
			cameraInputController.update();
		}
		camera.update();
		CursorHandler cursorHandler = handlers.getLogicHandlers().getCursorHandler();
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
		handlers.onTileSelected(texture);
	}

	@Override
	public void onEditModeSet(final EditModes mode) {
		handlers.onEditModeSet(mode);
	}


	@Override
	public void onTreeCharacterSelected(final CharacterDefinition definition) {
		handlers.onTreeCharacterSelected(definition);
	}

	@Override
	public void onSelectedObjectRotate(final int direction) {
		handlers.onSelectedObjectRotate(direction, mode);
	}

	@Override
	public void onTreeEnvSelected(final EnvironmentDefinitions env) {
		handlers.onTreeEnvSelected(env);
	}

	@Override
	public void onTreePickupSelected(final ItemDefinition definition) {
		handlers.onTreePickupSelected(definition);
	}

	@Override
	public void onCameraModeSet(final CameraModes mode) {
		handlers.onModeSet(mode);
	}

	@Override
	public void onSaveMapRequested( ) {
		handlers.getMapFileHandler().onSaveMapRequested(data);
	}

	@Override
	public void onNewMapRequested( ) {
		data.reset();
		initializeCameraPosition(camera);
		handlers.getRenderHandler().createModels(data.getMap().getDimension());
	}

	@Override
	public void onLoadMapRequested( ) {
		handlers.getMapFileHandler().onLoadMapRequested(data, wallCreator, handlers.getRenderHandler());
	}

	@Override
	public void onToolSet(final EditorTool tool) {
		handlers.onToolSet(tool);
	}

	@Override
	public void onNodeWallsDefined(final NodeWallsDefinitions definitions, final FlatNode src, final FlatNode dst) {
		handlers.getLogicHandlers().getActionsHandler().onNodeWallsDefined(definitions, src, dst);
	}

	@Override
	public void onTilesLift(final FlatNode src, final FlatNode dst, final float value) {
		handlers.getLogicHandlers().getActionsHandler().onTilesLift(src, dst, value);
	}

	@Override
	public float getAmbientLightValue( ) {
		return data.getMap().getAmbientLight();
	}

	@Override
	public void onAmbientLightValueSet(final float value) {
		data.getMap().setAmbientLight(value);
	}

	@Override
	public void onEnvObjectDefined(final PlacedEnvObject element, final float height) {
		handlers.getLogicHandlers().getActionsHandler().onEnvObjectDefined(element, height);
	}

	@Override
	public void onMapSizeSet(final int width, final int depth) {
		if (this.data.getMap().getNodes().length == depth && this.data.getMap().getNodes()[0].length == width) return;
		Dimension dimension = new Dimension(width, depth);
		data.getMap().resetSize(dimension);
		handlers.getRenderHandler().createModels(dimension);
	}

	@Override
	public Dimension getMapSize( ) {
		MapNodeData[][] nodes = data.getMap().getNodes();
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
		Set<MapNodeData> placedTiles = data.getPlacedElements().getPlacedTiles();
		GameAssetsManager assetsManager = handlers.getResourcesHandler().getAssetsManager();
		return handlers.getLogicHandlers().getActionsHandler().onTouchDown(assetsManager, placedTiles, button);
	}

	@Override
	public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button) {
		return handlers.onTouchUp(handlers.getLogicHandlers().getCursorHandler().getCursorTileModel());
	}

	@Override
	public boolean touchDragged(final int screenX, final int screenY, final int pointer) {
		boolean result = true;
		if (mode.getClass().equals(CameraModes.class)) {
			Vector3 rotationPoint = GeneralUtils.defineRotationPoint(auxVector3_1, camera);
			((CameraModes) mode).getManipulation().run(lastMouseTouchPosition, camera, screenX, screenY, rotationPoint);
		} else {
			result = handlers.getLogicHandlers().getCursorHandler().updateCursorByScreenCoords(screenX, screenY, camera, data.getMap());
		}
		lastMouseTouchPosition.set(screenX, screenY);
		return result;
	}

	@Override
	public boolean mouseMoved(final int screenX, final int screenY) {
		return handlers.getLogicHandlers().getCursorHandler().updateCursorByScreenCoords(screenX, screenY, camera, data.getMap());
	}

	public void subscribeForEvents(final MapManagerEventsSubscriber subscriber) {
		handlers.getEventsNotifier().subscribeForEvents(subscriber);
	}
}
