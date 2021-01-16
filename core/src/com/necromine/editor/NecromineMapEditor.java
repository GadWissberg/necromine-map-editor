package com.necromine.editor;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public class NecromineMapEditor extends ApplicationAdapter {
	public static final float FAR = 100f;
	private static final float NEAR = 0.01f;
	private static final Vector3 auxVector1 = new Vector3();
	private static final Vector3 auxVector2 = new Vector3();
	private static final int LEVEL_SIZE = 20;
	private static final Color GRID_COLOR = Color.GRAY;
	private static final float CAMERA_HEIGHT = 6;

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
		camera = createCamera();
		if (DefaultSettings.ENABLE_DEBUG_INPUT) {
			CameraInputController processor = new CameraInputController(camera);
			Gdx.input.setInputProcessor(processor);
			processor.autoUpdate = true;
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
		modelBatch.end();
	}

	private void update() {
		InputProcessor inputProcessor = Gdx.input.getInputProcessor();
		if (inputProcessor != null) {
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
	}
}
