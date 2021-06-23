package com.necromine.editor.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Disposable;

import java.awt.*;

public class ViewAuxHandler implements Disposable {
	private static final Color GRID_COLOR = Color.GRAY;

	private final AxisModelHandler axisModelHandler = new AxisModelHandler();
	private Model gridModel;
	private ModelInstance gridModelInstance;

	public void renderAux(final ModelBatch modelBatch) {
		axisModelHandler.render(modelBatch);
		modelBatch.render(gridModelInstance);
	}

	@Override
	public void dispose( ) {
		axisModelHandler.dispose();
		gridModel.dispose();
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
		Gdx.app.postRunnable(( ) -> {
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

}
