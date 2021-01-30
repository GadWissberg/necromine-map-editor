package com.necromine.editor;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.EnvironmentDefinitions;
import com.gadarts.necromine.model.characters.Direction;
import lombok.Getter;

@Getter
public class PlacedEnvObject extends PlacedElement {

	private static final Vector3 auxVector3_1 = new Vector3();
	private static final Vector2 auxVector2_1 = new Vector2();
	private final ModelInstance modelInstance;

	public PlacedEnvObject(final EnvironmentDefinitions definition,
						   final int row,
						   final int col,
						   final GameAssetsManager assetsManager,
						   final Direction selectedDirection) {
		super(row, col, definition, selectedDirection);
		this.modelInstance = new ModelInstance(assetsManager.getModel(definition.getModel()));
		modelInstance.transform.setTranslation(col, 0, row);
	}
}
