package com.necromine.editor;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.gadarts.necromine.Assets;
import com.gadarts.necromine.Assets.AssetsTypes;
import com.gadarts.necromine.assets.GameAssetsManager;

import java.util.HashSet;
import java.util.Set;

public class NecromineMapEditor extends ApplicationAdapter implements GuiEventsSubscriber, InputProcessor {
	public static final float FAR = 100f;
	public static final float FLICKER_RATE = 0.05f;
	public static final String TEMP_ASSETS_FOLDER = "C:\\Users\\gadw1\\StudioProjects\\isometric-game\\core\\assets";
	private static final float NEAR = 0.01f;
	private static final Vector3 auxVector1 = new Vector3();
	private static final Vector3 auxVector2 = new Vector3();
	private static final int LEVEL_SIZE = 20;
	private static final Color GRID_COLOR = Color.GRAY;
	private static final float CAMERA_HEIGHT = 6;
	private static final Plane auxPlane = new Plane();
	private static final Color CURSOR_COLOR = Color.valueOf("#2AFF14");
	public final int VIEWPORT_WIDTH;
	public final int VIEWPORT_HEIGHT;
	private final Tile[][] map;
	private final GameAssetsManager assetsManager;
	private final Set<Tile> initializedTiles = new HashSet<>();
	private ModelBatch modelBatch;
	private Model axisModelX;
	private ModelInstance axisModelInstanceX;
	private Model axisModelY;
	private ModelInstance axisModelInstanceY;
	private Model axisModelZ;
	private ModelInstance axisModelInstanceZ;
	private Camera camera;
	private Model gridModel;
	private ModelInstance gridModelInstance;
	private Assets.FloorsTextures selectedTile;
	private ModelInstance cursorTileModelInstance;
	private Model cursorTileModel;
	private ModelInstance cursorModelInstance;
	private float flicker;
	private MappingProcess currentProcess;

	public NecromineMapEditor(final int width, final int height) {
		VIEWPORT_WIDTH = width / 50;
		VIEWPORT_HEIGHT = height / 50;
		map = new Tile[LEVEL_SIZE][LEVEL_SIZE];
		assetsManager = new GameAssetsManager(TEMP_ASSETS_FOLDER.replace('\\', '/') + '/');
	}

	private void createAxis() {
		ModelBuilder modelBuilder = new ModelBuilder();
		axisModelX = createAxisModel(modelBuilder, auxVector2.set(1, 0, 0), Color.RED);
		axisModelInstanceX = new ModelInstance(axisModelX);
		axisModelY = createAxisModel(modelBuilder, auxVector2.set(0, 1, 0), Color.GREEN);
		axisModelInstanceY = new ModelInstance(axisModelY);
		axisModelZ = createAxisModel(modelBuilder, auxVector2.set(0, 0, 1), Color.BLUE);
		axisModelInstanceZ = new ModelInstance(axisModelZ);
		scaleAxis();
	}

	private void scaleAxis() {
		axisModelInstanceX.transform.scale(2, 2, 2);
		axisModelInstanceY.transform.scale(2, 2, 2);
		axisModelInstanceZ.transform.scale(2, 2, 2);
	}

	private Model createAxisModel(final ModelBuilder modelBuilder, final Vector3 dir, final Color color) {
		return modelBuilder.createArrow(
				auxVector1.setZero(),
				dir,
				new Material(ColorAttribute.createDiffuse(color)),
				Usage.Position | Usage.Normal
		);
	}

	@Override
	public void create() {
		this.modelBatch = new ModelBatch();
		assetsManager.loadGameFiles(AssetsTypes.FONT, AssetsTypes.MELODY, AssetsTypes.SOUND, AssetsTypes.SHADER);
		createAxis();
		createGrid();
		createCursorTile();
		camera = createCamera();
		initializeInput();
	}

	private void createCursorTile() {
		ModelBuilder builder = new ModelBuilder();
		Material material = new Material(ColorAttribute.createDiffuse(CURSOR_COLOR));
		BlendingAttribute highlightBlend = new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		material.set(highlightBlend);
		cursorTileModel = builder.createRect(
				1, 0, 0,
				0, 0, 0,
				0, 0, 1,
				1, 0, 1,
				0, 1, 0,
				material,
				Usage.Position | Usage.Normal | Usage.TextureCoordinates
		);
		cursorTileModelInstance = new ModelInstance(cursorTileModel);
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
		gridModelInstance.transform.translate(LEVEL_SIZE / 2f, 0, LEVEL_SIZE / 2f);
	}

	private Camera createCamera() {
		OrthographicCamera cam = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
		cam.near = NEAR;
		cam.far = FAR;
		cam.update();
		cam.position.set(4, CAMERA_HEIGHT, 4);
		cam.lookAt(auxVector1.setZero());
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
		modelBatch.begin(camera);
		modelBatch.render(gridModelInstance);
		renderAxis();
		if (cursorModelInstance != null) {
			modelBatch.render(cursorModelInstance);
		}
		renderExistingProcess();
		renderTiles();
		modelBatch.end();
	}

	private void renderTiles() {
		for (Tile tile : initializedTiles) {
			modelBatch.render(tile.getModelInstance());
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
		updateCursorFlicker();
	}

	private void updateCursorFlicker() {
		if (cursorModelInstance != null) {
			Material material = cursorModelInstance.materials.get(0);
			BlendingAttribute blend = (BlendingAttribute) material.get(BlendingAttribute.Type);
			blend.opacity = Math.abs(MathUtils.sin(flicker += FLICKER_RATE));
			material.set(blend);
		}
	}

	@Override
	public void dispose() {
		modelBatch.dispose();
		axisModelX.dispose();
		axisModelY.dispose();
		axisModelZ.dispose();
		gridModel.dispose();
		cursorTileModel.dispose();
		assetsManager.dispose();
	}

	@Override
	public void onTileSelected(final Assets.FloorsTextures texture) {
		selectedTile = texture;
		cursorModelInstance = cursorTileModelInstance;
	}

	@Override
	public void onModeChanged(EditorModes mode) {

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
		boolean result = false;
		if (cursorModelInstance == cursorTileModelInstance && currentProcess == null) {
			Vector3 position = cursorTileModelInstance.transform.getTranslation(auxVector1);
			PlaceTilesProcess placeTilesProcess = new PlaceTilesProcess(map);
			currentProcess = placeTilesProcess;
			placeTilesProcess.begin((int) position.z, (int) position.x, assetsManager, initializedTiles);
			result = true;
		}
		return result;
	}

	@Override
	public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button) {
		boolean result = false;
		if (currentProcess != null) {
			Vector3 position = cursorTileModelInstance.transform.getTranslation(auxVector1);
			PlaceTilesProcess currentProcess = (PlaceTilesProcess) this.currentProcess;
			currentProcess.finish((int) position.z, (int) position.x, selectedTile, cursorTileModel);
			this.currentProcess = null;
			result = true;
		}
		return result;
	}

	@Override
	public boolean touchDragged(final int screenX, final int screenY, final int pointer) {
		return updateCursorByScreenCoords(screenX, screenY);
	}

	@Override
	public boolean mouseMoved(final int screenX, final int screenY) {
		return updateCursorByScreenCoords(screenX, screenY);
	}

	private boolean updateCursorByScreenCoords(final int screenX, final int screenY) {
		if (cursorModelInstance != null) {
			Vector3 collisionPoint = castRayTowardsPlane(screenX, screenY);
			int x = MathUtils.clamp((int) collisionPoint.x, 0, LEVEL_SIZE);
			int z = MathUtils.clamp((int) collisionPoint.z, 0, LEVEL_SIZE);
			cursorModelInstance.transform.setTranslation(x, 0.01f, z);
			return true;
		}
		return false;
	}

	private void renderExistingProcess() {
		if (currentProcess != null) {
			if (currentProcess instanceof PlaceTilesProcess) {
				PlaceTilesProcess process = (PlaceTilesProcess) currentProcess;
				renderRectangleMarking(process.getSrcRow(), process.getSrcCol());
			}
		}
	}

	private void renderRectangleMarking(final int srcRow, final int srcCol) {
		Vector3 initialTilePos = cursorModelInstance.transform.getTranslation(auxVector1);
		for (int i = Math.min((int) initialTilePos.x, srcCol); i <= Math.max((int) initialTilePos.x, srcCol); i++) {
			for (int j = Math.min((int) initialTilePos.z, srcRow); j <= Math.max((int) initialTilePos.z, srcRow); j++) {
				cursorModelInstance.transform.setTranslation(i, initialTilePos.y, j);
				modelBatch.render(cursorModelInstance);
			}
		}
		cursorModelInstance.transform.setTranslation(initialTilePos);
	}

	Vector3 castRayTowardsPlane(final float screenX, final float screenY) {
		Ray ray = camera.getPickRay(screenX, screenY);
		auxPlane.set(Vector3.Zero, auxVector1.set(0, 1, 0));
		Intersector.intersectRayPlane(ray, auxPlane, auxVector2);
		return auxVector2;
	}

	@Override
	public boolean scrolled(final float amountX, final float amountY) {
		return false;
	}
}
