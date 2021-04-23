package com.necromine.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.gadarts.necromine.WallCreator;
import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.assets.Assets.AssetsTypes;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.ElementDefinition;
import com.gadarts.necromine.model.EnvironmentDefinitions;
import com.gadarts.necromine.model.MapNodeData;
import com.gadarts.necromine.model.characters.CharacterDefinition;
import com.gadarts.necromine.model.characters.CharacterTypes;
import com.gadarts.necromine.model.characters.Direction;
import com.gadarts.necromine.model.characters.SpriteType;
import com.gadarts.necromine.model.pickups.ItemDefinition;
import com.necromine.editor.handlers.CursorHandler;
import com.necromine.editor.handlers.Handlers;
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
import com.necromine.editor.utils.MapDeflater;
import com.necromine.editor.utils.MapInflater;
import com.necromine.editor.utils.Utils;
import lombok.Getter;

import java.awt.*;
import java.util.*;

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
	private static final int DEFAULT_LEVEL_SIZE = 20;

	@Getter
	private static EditorMode mode = EditModes.TILES;

	@Getter
	private static EditorTool tool = TilesTools.BRUSH;

	public final int VIEWPORT_WIDTH;
	public final int VIEWPORT_HEIGHT;
	private final GameAssetsManager assetsManager;
	private final PlacedElements placedElements = new PlacedElements();
	private final Vector2 lastMouseTouchPosition = new Vector2();
	private final Handlers handlers;
	private final MapInflater inflater;
	private final MapDeflater deflater = new MapDeflater();
	private final MapManagerEventsNotifier eventsNotifier = new MapManagerEventsNotifier();
	private final GameMap map = new GameMap(new Dimension(DEFAULT_LEVEL_SIZE, DEFAULT_LEVEL_SIZE));
	private WallCreator wallCreator;
	private MapRenderer renderer;
	private OrthographicCamera camera;
	private Assets.FloorsTextures selectedTile;
	private ElementDefinition selectedElement;
	private Model tileModel;

	public MapEditor(final int width, final int height, final String assetsLocation) {
		VIEWPORT_WIDTH = width / 50;
		VIEWPORT_HEIGHT = height / 50;
		assetsManager = new GameAssetsManager(assetsLocation.replace('\\', '/') + '/');
		handlers = new Handlers(assetsManager, map, eventsNotifier, placedElements);
		CursorHandler cursorHandler = handlers.getCursorHandler();
		cursorHandler.setCursorSelectionModel(new CursorSelectionModel(assetsManager));
		inflater = new MapInflater(assetsManager, cursorHandler, placedElements.getPlacedTiles());
		Arrays.stream(EditModes.values()).forEach(mode -> placedElements.getPlacedObjects().put(mode, new ArrayList<>()));
	}


	@Override
	public void create() {
		wallCreator = new WallCreator(assetsManager);
		camera = createCamera();
		renderer = new MapRenderer(assetsManager, handlers, camera);
		initializeGameFiles();
		tileModel = createRectModel();
		handlers.onCreate(tileModel, camera, wallCreator, new Dimension(DEFAULT_LEVEL_SIZE, DEFAULT_LEVEL_SIZE));
		initializeInput();
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

	private void initializeGameFiles() {
		assetsManager.loadGameFiles(AssetsTypes.FONT, AssetsTypes.MELODY, AssetsTypes.SOUND, AssetsTypes.SHADER);
		Arrays.stream(CharacterTypes.values()).forEach(type ->
				Arrays.stream(type.getDefinitions()).forEach(this::generateFramesMapForCharacter));
		postAssetsLoading();
	}

	private void postAssetsLoading() {
		Array<Model> models = new Array<>();
		assetsManager.getAll(Model.class, models);
		models.forEach(model -> model.materials.get(0).set(new BlendingAttribute()));
		Array<Texture> textures = new Array<>();
		assetsManager.getAll(Texture.class, textures);
		textures.forEach(texture -> texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat));
	}

	private void generateFramesMapForCharacter(final CharacterDefinition characterDefinition) {
		if (characterDefinition.getAtlasDefinition() == null) return;
		TextureAtlas atlas = assetsManager.getAtlas(characterDefinition.getAtlasDefinition());
		HashMap<Direction, TextureAtlas.AtlasRegion> playerFrames = new HashMap<>();
		Arrays.stream(Direction.values()).forEach(direction -> {
			String name = SpriteType.IDLE.name() + "_" + direction.name();
			playerFrames.put(direction, atlas.findRegion(name.toLowerCase()));
		});
		String format = String.format(Utils.FRAMES_KEY_CHARACTER, characterDefinition.getCharacterType().name());
		assetsManager.addAsset(format, Map.class, playerFrames);
	}


	private OrthographicCamera createCamera() {
		OrthographicCamera cam = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
		cam.near = NEAR;
		cam.far = FAR;
		cam.update();
		cam.position.set(4, 6, 4);
		cam.lookAt(auxVector3_1.setZero());
		cam.position.set(4, CAMERA_HEIGHT, 4);
		return cam;
	}

	@Override
	public void render() {
		update();
		renderer.draw(mode, placedElements, placedElements.getPlacedTiles(), selectedElement);
	}


	private void update() {
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
	public void dispose() {
		handlers.dispose();
		assetsManager.dispose();
		tileModel.dispose();
		wallCreator.dispose();
	}

	@Override
	public void onTileSelected(final Assets.FloorsTextures texture) {
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
		applyOpacity();
	}

	@Override
	public void onTreePickupSelected(final ItemDefinition definition) {
		selectedElement = definition;
		handlers.onTreePickupSelected(selectedElement, definition);
		applyOpacity();
	}

	@Override
	public void onCameraModeSet(final CameraModes mode) {
		onModeSet(mode);
	}

	@Override
	public void onSaveMapRequested() {
		deflater.deflate(map, placedElements);
	}

	@Override
	public void onLoadMapRequested() {
		inflater.inflateMap(map, placedElements, wallCreator, handlers.getViewAuxHandler());
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
		handlers.getActionsHandler().onNodeWallsDefined(definitions, src, dst, assetsManager);
	}

	@Override
	public void onTilesLift(final FlatNode src, final FlatNode dst, final float value) {
		handlers.getActionsHandler().onTilesLift(src, dst, value);
	}

	@Override
	public float getAmbientLightValue() {
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
	public Dimension getMapSize() {
		MapNodeData[][] nodes = map.getNodes();
		return new Dimension(nodes.length, nodes[0].length);
	}


	private void applyOpacity() {
		ModelInstance modelInstance = handlers.getCursorHandler().getCursorSelectionModel().getModelInstance();
		BlendingAttribute blend = (BlendingAttribute) modelInstance.materials.get(0).get(BlendingAttribute.Type);
		blend.opacity = CursorHandler.CURSOR_OPACITY;
	}


	void initializeInput() {
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
		return handlers.getActionsHandler().onTouchDown(assetsManager, placedTiles, button);
	}

	@Override
	public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button) {
		return handlers.getActionsHandler().onTouchUp(selectedTile, handlers.getCursorHandler().getCursorTileModel());
	}

	@Override
	public boolean touchDragged(final int screenX, final int screenY, final int pointer) {
		boolean result;
		if (mode.getClass().equals(CameraModes.class)) {
			CameraModes cameraMode = (CameraModes) mode;
			cameraMode.getManipulation().run(lastMouseTouchPosition, camera, screenX, screenY);
			result = true;
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
