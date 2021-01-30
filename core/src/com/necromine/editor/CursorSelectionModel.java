package com.necromine.editor;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.EnvironmentDefinitions;
import com.gadarts.necromine.model.characters.Direction;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import static com.gadarts.necromine.model.characters.Direction.EAST;

@RequiredArgsConstructor
@Getter
public class CursorSelectionModel {

	@Setter
	private Direction facingDirection = EAST;

	private static final Vector2 auxVector2_1 = new Vector2();
	private static final Vector3 auxVector3_1 = new Vector3();
	private static final Vector3 auxVector3_2 = new Vector3();
	private final GameAssetsManager assetsManager;
	private ModelInstance modelInstance;
	private EnvironmentDefinitions selectedElement;


	public void setSelection(final EnvironmentDefinitions selectedElement) {
		this.selectedElement = selectedElement;
		modelInstance = new ModelInstance(assetsManager.getModel(selectedElement.getModel()));
		facingDirection = EAST;
	}
}
