package com.necromine.editor;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.EnvironmentDefinitions;
import com.gadarts.necromine.model.characters.CharacterDefinition;
import com.gadarts.necromine.model.characters.Direction;
import lombok.Getter;

@Getter
public class PlacedEnvObject extends PlacedElement {

	private static final Vector3 auxVector = new Vector3();
	private final ModelInstance modelInstance;

	public PlacedEnvObject(final EnvironmentDefinitions definition,
						   final int row,
						   final int col,
						   final GameAssetsManager assetsManager,
						   final Direction selectedCharacterDirection) {
		super(row, col, selectedCharacterDirection);
		this.modelInstance = new ModelInstance(assetsManager.getModel(definition.getModel()));
		modelInstance.transform.setTranslation(col, 0, row).translate(definition.getOffset(auxVector));
	}
}
