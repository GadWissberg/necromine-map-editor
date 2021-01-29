package com.necromine.editor;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.EnvironmentDefinitions;
import com.gadarts.necromine.model.characters.Direction;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.gadarts.necromine.model.characters.Direction.EAST;

@RequiredArgsConstructor
@Getter
public class CursorSelectionModel {

	private static final Vector2 auxVector2_1 = new Vector2();
	private static final Vector3 auxVector3_1 = new Vector3();
	private static final Vector3 auxVector3_2 = new Vector3();
	private final GameAssetsManager assetsManager;
	private ModelInstance modelInstance;
	private Direction facingDirection = EAST;
	private EnvironmentDefinitions selectedElement;

	public void setDirection(final Direction value) {
		Vector3 originalLocation = modelInstance.transform.getTranslation(auxVector3_1);
		modelInstance.transform.setToRotation(Vector3.Y, value.getDirection(auxVector2_1).angleDeg());
		modelInstance.transform.setTranslation(originalLocation);
		facingDirection = value;
	}

	public void setSelection(final EnvironmentDefinitions selectedElement) {
		this.selectedElement = selectedElement;
		modelInstance = new ModelInstance(assetsManager.getModel(selectedElement.getModel()));
	}
}
