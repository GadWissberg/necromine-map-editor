package com.necromine.editor.model;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.ModelElementDefinition;
import com.gadarts.necromine.model.characters.Direction;
import lombok.Getter;

@Getter
public abstract class PlacedModelElement extends PlacedElement {
	protected final ModelInstance modelInstance;

	public PlacedModelElement(final int row,
							  final int col,
							  final ModelElementDefinition definition,
							  final Direction selectedDirection,
							  final GameAssetsManager assetsManager) {
		super(row, col, definition, selectedDirection);
		this.modelInstance = new ModelInstance(assetsManager.getModel(definition.getModelDefinition()));
		modelInstance.transform.setTranslation(col, 0, row);
	}
}
