package com.necromine.editor;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.gadarts.necromine.Assets;
import lombok.Setter;

public class NecromineMapEditor extends ApplicationAdapter implements GuiEventsSubscriber, InputProcessor {
	public static final float FAR = 100f;
	private static final float NEAR = 0.01f;
	private static final Vector3 auxVector1 = new Vector3();
	private static final Vector3 auxVector2 = new Vector3();
	private static final int LEVEL_SIZE = 20;
	private static final Color GRID_COLOR = Color.GRAY;
	private static final float CAMERA_HEIGHT = 6;
	private static final Plane auxPlane = new Plane();
	private static final Color CURSOR_COLOR = Color.valueOf("#2AFF14");
	@Setter
	private static EditorModes mode;
	public final int VIEWPORT_WIDTH;
	public final int VIEWPORT_HEIGHT;
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

	public NecromineMapEditor(final int width, final int height) {
		VIEWPORT_WIDTH = width / 50;
		VIEWPORT_HEIGHT = height / 50;
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
		createAxis();
		createGrid();
		createCursorTile();
		camera = createCamera();
		initializeInput();
	}

	private void createCursorTile() {
		ModelBuilder builder = new ModelBuilder();
		Material material = new Material(ColorAttribute.createDiffuse(CURSOR_COLOR));
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
		modelBatch.render(axisModelInstanceX);
		modelBatch.render(axisModelInstanceY);
		modelBatch.render(axisModelInstanceZ);
		if (cursorModelInstance != null) {
			modelBatch.render(cursorModelInstance);
		}
		modelBatch.end();
	}

	private void update() {
		InputProcessor inputProcessor = Gdx.input.getInputProcessor();
		if (inputProcessor != null && DefaultSettings.ENABLE_DEBUG_INPUT) {
			CameraInputController cameraInputController = (CameraInputController) inputProcessor;
			cameraInputController.update();
		}
		camera.update();
	}

	@Override
	public void dispose() {
		modelBatch.dispose();
		axisModelX.dispose();
		axisModelY.dispose();
		axisModelZ.dispose();
		gridModel.dispose();
		cursorTileModel.dispose();
	}

	@Override
	public void onTileSelected(final Assets.FloorsTextures texture) {
		setMode(EditorModes.TILES);
		selectedTile = texture;
		cursorModelInstance = cursorTileModelInstance;
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
		return false;
	}

	@Override
	public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button) {
		return false;
	}

	@Override
	public boolean touchDragged(final int screenX, final int screenY, final int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(final int screenX, final int screenY) {
		boolean result = false;
		if (cursorModelInstance != null) {
			Vector3 collisionPoint = castRayTowardsPlane(screenX, screenY);
			int x = MathUtils.clamp((int) collisionPoint.x, 0, LEVEL_SIZE);
			int z = MathUtils.clamp((int) collisionPoint.z, 0, LEVEL_SIZE);
			cursorModelInstance.transform.setTranslation(x, 0, z);
			result = true;
		}
		return result;
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
