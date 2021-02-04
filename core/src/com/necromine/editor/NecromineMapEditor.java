package com.necromine.editor;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntSet;
import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.assets.Assets.AssetsTypes;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.ElementDefinition;
import com.gadarts.necromine.model.EnvironmentDefinitions;
import com.gadarts.necromine.model.characters.CharacterDefinition;
import com.gadarts.necromine.model.characters.CharacterTypes;
import com.gadarts.necromine.model.characters.Direction;
import com.gadarts.necromine.model.characters.SpriteType;
import com.gadarts.necromine.model.pickups.ItemDefinition;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.necromine.editor.actions.ActionsHandler;
import com.necromine.editor.actions.processes.MappingProcess;
import com.necromine.editor.actions.processes.PlaceTilesProcess;
import com.necromine.editor.model.*;
import lombok.Getter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.*;
import java.util.stream.IntStream;

import static com.gadarts.necromine.model.characters.CharacterTypes.BILLBOARD_Y;
import static com.gadarts.necromine.model.characters.CharacterTypes.PLAYER;
import static com.gadarts.necromine.model.characters.Direction.*;

public class NecromineMapEditor extends ApplicationAdapter implements GuiEventsSubscriber, InputProcessor {
	public static final float FAR = 200f;
	public static final float FLICKER_RATE = 0.05f;
	public static final String TEMP_ASSETS_FOLDER = "C:\\Users\\gadw1\\StudioProjects\\isometric-game\\core\\assets";
	public static final float CURSOR_Y = 0.01f;
	public static final int LEVEL_SIZE = 20;
	static final Vector3 auxVector3_1 = new Vector3();
	private static final int DECALS_POOL_SIZE = 200;
	private static final float NEAR = 0.01f;
	private static final Vector3 auxVector3_2 = new Vector3();
	private static final Vector3 auxVector3_3 = new Vector3();
	private static final Vector2 auxVector2_1 = new Vector2();
	private static final Color GRID_COLOR = Color.GRAY;
	private static final float CAMERA_HEIGHT = 6;
	private static final Plane auxPlane = new Plane();
	private static final Color CURSOR_COLOR = Color.valueOf("#2AFF14");
	private static final float CURSOR_OPACITY = 0.5f;
	private static final Matrix4 auxMatrix = new Matrix4();
	private static final String KEY_TARGET = "target";
	private static final int TARGET_VERSION = 5;
	private static final String KEY_TILES = "tiles";
	private static final String KEY_WIDTH = "width";
	private static final String KEY_DEPTH = "depth";
	private static final String KEY_MATRIX = "matrix";
	private static final String KEY_ROW = "row";
	private static final String KEY_COL = "col";
	private static final String KEY_DIRECTION = "direction";
	private static final String KEY_PLAYER = "player";
	@Getter
	private static EditorMode mode = EditModes.TILES;
	public final int VIEWPORT_WIDTH;
	public final int VIEWPORT_HEIGHT;
	private final MapNode[][] map;
	private final GameAssetsManager assetsManager;
	private final Set<MapNode> initializedTiles = new HashSet<>();
	private final Map<EditModes, List<? extends PlacedElement>> placedElements = new HashMap<>();
	private final CursorSelectionModel cursorSelectionModel;
	private final Vector2 lastMouseTouchPosition = new Vector2();
	private ActionsHandler actionsHandler;
	private ModelBatch modelBatch;
	private Model axisModelX;
	private ModelInstance axisModelInstanceX;
	private Model axisModelY;
	private ModelInstance axisModelInstanceY;
	private Model axisModelZ;
	private ModelInstance axisModelInstanceZ;
	private OrthographicCamera camera;
	private Model gridModel;
	private ModelInstance gridModelInstance;
	private Assets.FloorsTextures selectedTile;
	private ModelInstance cursorTileModelInstance;
	private Model cursorTileModel;
	private ModelInstance highlighter;
	private float flicker;
	private ElementDefinition selectedElement;
	private CharacterDecal cursorCharacterDecal;
	private DecalBatch decalBatch;
	private Decal cursorSimpleDecal;
	private Gson gson = new Gson();

	public NecromineMapEditor(final int width, final int height) {
		VIEWPORT_WIDTH = width / 50;
		VIEWPORT_HEIGHT = height / 50;
		map = new MapNode[LEVEL_SIZE][LEVEL_SIZE];
		assetsManager = new GameAssetsManager(TEMP_ASSETS_FOLDER.replace('\\', '/') + '/');
		cursorSelectionModel = new CursorSelectionModel(assetsManager);
		Arrays.stream(EditModes.values()).forEach(mode -> placedElements.put(mode, new ArrayList<>()));
	}

	private void createAxis() {
		ModelBuilder modelBuilder = new ModelBuilder();
		axisModelX = createAxisModel(modelBuilder, auxVector3_2.set(1, 0, 0), Color.RED);
		axisModelInstanceX = new ModelInstance(axisModelX);
		axisModelY = createAxisModel(modelBuilder, auxVector3_2.set(0, 1, 0), Color.GREEN);
		axisModelInstanceY = new ModelInstance(axisModelY);
		axisModelZ = createAxisModel(modelBuilder, auxVector3_2.set(0, 0, 1), Color.BLUE);
		axisModelInstanceZ = new ModelInstance(axisModelZ);
		scaleAxis();
	}

	private void scaleAxis() {
		axisModelInstanceX.transform.scale(2, 2, 2);
		axisModelInstanceX.transform.translate(0, 0.2f, 0);
		axisModelInstanceY.transform.scale(2, 2, 2);
		axisModelInstanceY.transform.translate(0, 0.2f, 0);
		axisModelInstanceZ.transform.scale(2, 2, 2);
		axisModelInstanceZ.transform.translate(0, 0.2f, 0);
	}

	private Model createAxisModel(final ModelBuilder modelBuilder, final Vector3 dir, final Color color) {
		return modelBuilder.createArrow(
				auxVector3_1.setZero(),
				dir,
				new Material(ColorAttribute.createDiffuse(color)),
				Usage.Position | Usage.Normal
		);
	}

	@Override
	public void create() {
		camera = createCamera();
		createBatches();
		initializeGameFiles();
		createAxis();
		createGrid();
		createCursors();
		createActionsHandler();
		initializeInput();
	}

	private void createActionsHandler() {
		actionsHandler = new ActionsHandler(cursorTileModelInstance, map, placedElements);
		actionsHandler.setCursorCharacterDecal(cursorCharacterDecal);
		actionsHandler.setCursorSelectionModel(cursorSelectionModel);
	}

	private void initializeGameFiles() {
		assetsManager.loadGameFiles(AssetsTypes.FONT, AssetsTypes.MELODY, AssetsTypes.SOUND, AssetsTypes.SHADER);
		Arrays.stream(CharacterTypes.values()).forEach(type ->
				Arrays.stream(type.getDefinitions()).forEach(this::generateFramesMapForCharacter));
		Array<Model> models = new Array<>();
		assetsManager.getAll(Model.class, models);
		models.forEach(model -> model.materials.get(0).set(new BlendingAttribute()));
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

	private void createBatches() {
		CameraGroupStrategy groupStrategy = new CameraGroupStrategy(camera);
		this.decalBatch = new DecalBatch(DECALS_POOL_SIZE, groupStrategy);
		this.modelBatch = new ModelBatch();
	}

	private void createCursors() {
		createCursorTile();
		createCursorCharacterDecal();
		createCursorSimpleDecal();
	}

	private void createCursorTile() {
		cursorTileModel = createRectModel();
		cursorTileModel.materials.get(0).set(ColorAttribute.createDiffuse(CURSOR_COLOR));
		cursorTileModelInstance = new ModelInstance(cursorTileModel);
	}

	private void createCursorCharacterDecal() {
		cursorCharacterDecal = Utils.createCharacterDecal(
				assetsManager,
				CharacterTypes.PLAYER.getDefinitions()[0],
				0,
				0,
				SOUTH);
		Color color = cursorCharacterDecal.getDecal().getColor();
		cursorCharacterDecal.getDecal().setColor(color.r, color.g, color.b, CURSOR_OPACITY);
	}

	private void createCursorSimpleDecal() {
		Texture bulb = assetsManager.getTexture(Assets.UiTextures.BULB);
		cursorSimpleDecal = Utils.createSimpleDecal(bulb);
		Color color = cursorSimpleDecal.getColor();
		cursorSimpleDecal.setColor(color.r, color.g, color.b, CURSOR_OPACITY);
	}

	private Model createRectModel() {
		ModelBuilder builder = new ModelBuilder();
		BlendingAttribute highlightBlend = new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Material material = new Material(highlightBlend);
		return builder.createRect(
				1, 0, 0,
				0, 0, 0,
				0, 0, 1,
				1, 0, 1,
				0, 1, 0,
				material,
				Usage.Position | Usage.Normal | Usage.TextureCoordinates
		);
	}

	private void initializeInput() {
		if (DefaultSettings.ENABLE_DEBUG_INPUT) {
			CameraInputController processor = new CameraInputController(camera);
			Gdx.input.setInputProcessor(processor);
			processor.autoUpdate = true;
		} else {
			Gdx.input.setInputProcessor(this);
		}
	}

	private void createGrid() {
		ModelBuilder builder = new ModelBuilder();
		Material material = new Material(ColorAttribute.createDiffuse(GRID_COLOR));
		int attributes = Usage.Position | Usage.Normal;
		gridModel = builder.createLineGrid(LEVEL_SIZE, LEVEL_SIZE, 1, 1, material, attributes);
		gridModelInstance = new ModelInstance(gridModel);
		gridModelInstance.transform.translate(LEVEL_SIZE / 2f, 0.01f, LEVEL_SIZE / 2f);
	}

	private OrthographicCamera createCamera() {
		OrthographicCamera cam = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
		cam.near = NEAR;
		cam.far = FAR;
		cam.update();
		cam.position.set(4, CAMERA_HEIGHT, 4);
		cam.lookAt(auxVector3_1.setZero());
		return cam;
	}

	@Override
	public void render() {
		update();
		draw();
	}

	private void draw() {
		int sam = Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0;
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | sam);
		renderWorld();
	}

	private void renderWorld() {
		renderModels();
		renderDecals();
	}

	private void renderDecals() {
		Gdx.gl.glDepthMask(false);
		if (highlighter != null && mode.getClass().equals(EditModes.class) && ((EditModes) mode).isDecalCursor()) {
			renderCursorOfDecalMode();
		}
		renderDecalPlacedElements();
		decalBatch.flush();
		Gdx.gl.glDepthMask(true);
	}

	private void renderDecalPlacedElements() {
		List<PlacedCharacter> placedCharacters = (List<PlacedCharacter>) placedElements.get(EditModes.CHARACTERS);
		for (PlacedCharacter character : placedCharacters) {
			renderCharacter(character);
		}
		List<PlacedLight> placedLights = (List<PlacedLight>) placedElements.get(EditModes.LIGHTS);
		for (PlacedLight placedLight : placedLights) {
			renderDecal(placedLight.getDecal());
		}
	}

	private void renderCursorOfDecalMode() {
		if (mode == EditModes.CHARACTERS) {
			renderCharacter(cursorCharacterDecal, cursorCharacterDecal.getSpriteDirection());
		} else {
			renderDecal(cursorSimpleDecal);
		}
	}

	private void renderCharacter(final PlacedCharacter character) {
		renderCharacter(character.getCharacterDecal(), character.getCharacterDecal().getSpriteDirection());
	}

	private void renderCharacter(final CharacterDecal characterDecal, final Direction facingDirection) {
		Utils.applyFrameSeenFromCameraForCharacterDecal(characterDecal, facingDirection, camera, assetsManager);
		renderDecal(characterDecal.getDecal());
	}


	private void renderDecal(final Decal decal) {
		decal.lookAt(auxVector3_1.set(decal.getPosition()).sub(camera.direction), camera.up);
		decalBatch.add(decal);
	}

	private void renderModels() {
		modelBatch.begin(camera);
		modelBatch.render(gridModelInstance);
		renderAxis();
		renderCursor();
		renderExistingProcess();
		renderModelPlacedElements();
		modelBatch.end();
	}

	private void renderModelPlacedElements() {
		renderTiles();
		renderEnvObjects();
		renderPickups();
	}

	private void renderCursor() {
		if (highlighter == null) return;
		if (mode != EditModes.ENVIRONMENT) {
			modelBatch.render(highlighter);
		}
		renderCursorObjectModel();
	}

	private void renderCursorObjectModel() {
		if (selectedElement != null) {
			if (mode == EditModes.ENVIRONMENT) {
				renderModelCursorFloorGrid();
				EnvironmentDefinitions selectedElement = (EnvironmentDefinitions) cursorSelectionModel.getSelectedElement();
				ModelInstance modelInstance = cursorSelectionModel.getModelInstance();
				renderEnvObject(selectedElement, modelInstance, cursorSelectionModel.getFacingDirection());
			} else if (mode == EditModes.PICKUPS) {
				renderPickup(cursorSelectionModel.getModelInstance());
			}
		}
	}

	@SuppressWarnings("SuspiciousNameCombination")
	private void renderModelCursorFloorGrid() {
		Vector3 originalPosition = cursorTileModelInstance.transform.getTranslation(auxVector3_1);
		Vector3 cursorPosition = highlighter.transform.getTranslation(auxVector3_3);
		cursorPosition.y = CURSOR_Y;
		EnvironmentDefinitions def = (EnvironmentDefinitions) selectedElement;
		Direction facingDirection = cursorSelectionModel.getFacingDirection();
		int halfWidth = def.getWidth() / 2;
		int halfHeight = def.getHeight() / 2;
		if (facingDirection == NORTH || facingDirection == SOUTH) {
			int swap = halfWidth;
			halfWidth = halfHeight;
			halfHeight = swap;
		}
		for (int row = -halfHeight; row < Math.max(halfHeight, 1); row++) {
			for (int col = -halfWidth; col < Math.max(halfWidth, 1); col++) {
				cursorTileModelInstance.transform.setTranslation(cursorPosition).translate(col, 0, row);
				modelBatch.render(cursorTileModelInstance);
			}
		}
		cursorTileModelInstance.transform.setTranslation(originalPosition);
	}


	private void renderTiles() {
		for (MapNode tile : initializedTiles) {
			if (tile.getModelInstance() != null) {
				modelBatch.render(tile.getModelInstance());
			}
		}
	}

	private void renderEnvObjects() {
		List<PlacedEnvObject> placedEnvObjects = (List<PlacedEnvObject>) placedElements.get(EditModes.ENVIRONMENT);
		for (PlacedEnvObject placedEnvObject : placedEnvObjects) {
			renderEnvObject(
					(EnvironmentDefinitions) placedEnvObject.getDefinition(),
					placedEnvObject.getModelInstance(),
					placedEnvObject.getFacingDirection());
		}
	}

	private void renderPickups() {
		List<PlacedPickup> placedElements = (List<PlacedPickup>) this.placedElements.get(EditModes.PICKUPS);
		for (PlacedPickup pickup : placedElements) {
			renderPickup(pickup.getModelInstance());
		}
	}

	private void renderPickup(final ModelInstance modelInstance) {
		Matrix4 originalTransform = auxMatrix.set(modelInstance.transform);
		modelInstance.transform.translate(0.5f, 0, 0.5f);
		modelBatch.render(modelInstance);
		modelInstance.transform.set(originalTransform);
	}

	private void renderEnvObject(final EnvironmentDefinitions definition,
								 final ModelInstance modelInstance,
								 final Direction facingDirection) {
		Matrix4 originalTransform = auxMatrix.set(modelInstance.transform);
		modelInstance.transform.translate(0.5f, 0, 0.5f);
		modelInstance.transform.rotate(Vector3.Y, -1 * facingDirection.getDirection(auxVector2_1).angleDeg());
		modelInstance.transform.translate(definition.getOffset(auxVector3_1));
		handleEvenSize(definition, modelInstance, facingDirection);
		modelBatch.render(modelInstance);
		modelInstance.transform.set(originalTransform);
	}

	private void handleEvenSize(final EnvironmentDefinitions definition,
								final ModelInstance modelInstance,
								final Direction facingDirection) {
		boolean handleEvenSize = facingDirection == EAST || facingDirection == NORTH;
		if (definition.getWidth() % 2 == 0) {
			modelInstance.transform.translate(0.5f * (handleEvenSize ? -1 : 1), 0, 0);
		}
		if (definition.getHeight() % 2 == 0) {
			modelInstance.transform.translate(0, 0, 0.5f * (handleEvenSize ? -1 : 1));
		}
	}

	private void renderAxis() {
		modelBatch.render(axisModelInstanceX);
		modelBatch.render(axisModelInstanceY);
		modelBatch.render(axisModelInstanceZ);
	}

	private void update() {
		InputProcessor inputProcessor = Gdx.input.getInputProcessor();
		if (inputProcessor != null && DefaultSettings.ENABLE_DEBUG_INPUT) {
			CameraInputController cameraInputController = (CameraInputController) inputProcessor;
			cameraInputController.update();
		}
		camera.update();
		if (highlighter != null) {
			updateCursorFlicker();
		}
	}

	private void updateCursorFlicker() {
		Material material;
		if ((mode == EditModes.TILES || mode == EditModes.CHARACTERS)) {
			material = highlighter.materials.get(0);
		} else {
			material = cursorTileModelInstance.materials.get(0);
		}
		BlendingAttribute blend = (BlendingAttribute) material.get(BlendingAttribute.Type);
		blend.opacity = Math.abs(MathUtils.sin(flicker += FLICKER_RATE));
		material.set(blend);
	}

	@Override
	public void dispose() {
		modelBatch.dispose();
		axisModelX.dispose();
		axisModelY.dispose();
		axisModelZ.dispose();
		gridModel.dispose();
		cursorTileModel.dispose();
		decalBatch.dispose();
		assetsManager.dispose();
	}

	@Override
	public void onTileSelected(final Assets.FloorsTextures texture) {
		selectedTile = texture;
		highlighter = cursorTileModelInstance;
		actionsHandler.setSelectedTile(selectedTile);
	}

	@Override
	public void onEditModeSet(final EditModes mode) {
		onModeSet(mode);
		highlighter = cursorTileModelInstance;
	}

	private void onModeSet(final EditorMode mode) {
		selectedElement = null;
		highlighter = null;
		NecromineMapEditor.mode = mode;
	}

	@Override
	public void onTreeCharacterSelected(final CharacterDefinition definition) {
		selectedElement = definition;
		actionsHandler.setSelectedElement(selectedElement);
		cursorCharacterDecal.setCharacterDefinition(definition);
	}

	@Override
	public void onSelectedObjectRotate(final int direction) {
		if (selectedElement != null) {
			if (mode == EditModes.CHARACTERS) {
				int ordinal = cursorCharacterDecal.getSpriteDirection().ordinal() + direction;
				int length = Direction.values().length;
				int index = (ordinal < 0 ? ordinal + length : ordinal) % length;
				cursorCharacterDecal.setSpriteDirection(Direction.values()[index]);
			} else {
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
		highlighter = cursorTileModelInstance;
		actionsHandler.setSelectedElement(selectedElement);
		cursorSelectionModel.setSelection(selectedElement, ((EnvironmentDefinitions) selectedElement).getModelDefinition());
		applyOpacity();
	}

	@Override
	public void onTreePickupSelected(final ItemDefinition definition) {
		selectedElement = definition;
		highlighter = cursorTileModelInstance;
		actionsHandler.setSelectedElement(selectedElement);
		cursorSelectionModel.setSelection(selectedElement, definition.getModelDefinition());
		applyOpacity();
	}

	@Override
	public void onCameraModeSet(final CameraModes mode) {
		onModeSet(mode);
	}

	@Override
	public void onSaveMapRequested() {
		JsonObject output = new JsonObject();
		output.addProperty(KEY_TARGET, TARGET_VERSION);
		JsonObject tiles = createTilesData();
		output.add(KEY_TILES, tiles);
		List<? extends PlacedElement> placedCharacters = this.placedElements.get(EditModes.CHARACTERS);
		placedCharacters.stream().filter(character ->
				((CharacterDefinition) character.getDefinition()).getCharacterType().equals(PLAYER))
				.findFirst()
				.ifPresent(player -> {
					JsonObject playerJsonObject = new JsonObject();
					playerJsonObject.addProperty(KEY_ROW, player.getRow());
					playerJsonObject.addProperty(KEY_COL, player.getCol());
					playerJsonObject.addProperty(KEY_DIRECTION, player.getFacingDirection().ordinal());
					output.add(KEY_PLAYER, playerJsonObject);
				});
		try (Writer writer = new FileWriter("test_map.json")) {
			gson.toJson(output, writer);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	private JsonObject createTilesData() {
		JsonObject tiles = new JsonObject();
		tiles.addProperty(KEY_WIDTH, LEVEL_SIZE);
		tiles.addProperty(KEY_DEPTH, LEVEL_SIZE);
		StringBuilder builder = new StringBuilder();
		IntStream.range(0, LEVEL_SIZE).forEach(row ->
				IntStream.range(0, LEVEL_SIZE).forEach(col -> {
					MapNode mapNode = map[row][col];
					if (mapNode != null) {
						builder.append(mapNode.getTextureDefinition().ordinal() + 1);
					} else {
						builder.append(0);
					}
				})
		);
		tiles.addProperty(KEY_MATRIX, builder.toString());
		return tiles;
	}

	private void applyOpacity() {
		ModelInstance modelInstance = cursorSelectionModel.getModelInstance();
		BlendingAttribute blend = (BlendingAttribute) modelInstance.materials.get(0).get(BlendingAttribute.Type);
		blend.opacity = CURSOR_OPACITY;
	}


	@Override
	public boolean keyDown(final int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(final int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(final char character) {
		return false;
	}

	@Override
	public boolean touchDown(final int screenX, final int screenY, final int pointer, final int button) {
		lastMouseTouchPosition.set(screenX, screenY);
		return actionsHandler.onTouchDown(assetsManager, initializedTiles);
	}


	@Override
	public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button) {
		return actionsHandler.onTouchUp(selectedTile, cursorTileModel);
	}

	@Override
	public boolean touchDragged(final int screenX, final int screenY, final int pointer) {
		boolean result;
		if (mode.getClass().equals(CameraModes.class)) {
			CameraModes cameraMode = (CameraModes) mode;
			cameraMode.getManipulation().run(lastMouseTouchPosition, camera, screenX, screenY);
			result = true;
		} else {
			result = updateCursorByScreenCoords(screenX, screenY);
		}
		lastMouseTouchPosition.set(screenX, screenY);
		return result;
	}

	@Override
	public boolean mouseMoved(final int screenX, final int screenY) {
		return updateCursorByScreenCoords(screenX, screenY);
	}

	private boolean updateCursorByScreenCoords(final int screenX, final int screenY) {
		if (highlighter != null) {
			Vector3 collisionPoint = castRayTowardsPlane(screenX, screenY);
			int x = MathUtils.clamp((int) collisionPoint.x, 0, LEVEL_SIZE);
			int z = MathUtils.clamp((int) collisionPoint.z, 0, LEVEL_SIZE);
			highlighter.transform.setTranslation(x, 0.01f, z);
			updateCursorAdditionals(x, z);
			return true;
		}
		return false;
	}

	private void updateCursorAdditionals(final int x, final int z) {
		ModelInstance modelInstance = cursorSelectionModel.getModelInstance();
		if (modelInstance != null) {
			modelInstance.transform.setTranslation(x, 0.01f, z);
		}
		updateCursorOfDecalMode(x, z);
	}

	private void updateCursorOfDecalMode(final int x, final int z) {
		if (mode == EditModes.CHARACTERS) {
			cursorCharacterDecal.getDecal().setPosition(x + 0.5f, BILLBOARD_Y, z + 0.5f);
		} else {
			cursorSimpleDecal.setPosition(x + 0.5f, BILLBOARD_Y, z + 0.5f);
		}
	}


	private void renderExistingProcess() {
		MappingProcess<? extends MappingProcess.FinishProcessParameters> currentProcess = actionsHandler.getCurrentProcess();
		if (currentProcess != null) {
			if (currentProcess instanceof PlaceTilesProcess) {
				PlaceTilesProcess process = (PlaceTilesProcess) currentProcess;
				renderRectangleMarking(process.getSrcRow(), process.getSrcCol());
			}
		}
	}

	private void renderRectangleMarking(final int srcRow, final int srcCol) {
		Vector3 initialTilePos = highlighter.transform.getTranslation(auxVector3_1);
		for (int i = Math.min((int) initialTilePos.x, srcCol); i <= Math.max((int) initialTilePos.x, srcCol); i++) {
			for (int j = Math.min((int) initialTilePos.z, srcRow); j <= Math.max((int) initialTilePos.z, srcRow); j++) {
				highlighter.transform.setTranslation(i, initialTilePos.y, j);
				modelBatch.render(highlighter);
			}
		}
		highlighter.transform.setTranslation(initialTilePos);
	}

	Vector3 castRayTowardsPlane(final float screenX, final float screenY) {
		Ray ray = camera.getPickRay(screenX, screenY);
		auxPlane.set(Vector3.Zero, auxVector3_1.set(0, 1, 0));
		Intersector.intersectRayPlane(ray, auxPlane, auxVector3_2);
		return auxVector3_2;
	}

	@Override
	public boolean scrolled(final float amountX, final float amountY) {
		return false;
	}
}
