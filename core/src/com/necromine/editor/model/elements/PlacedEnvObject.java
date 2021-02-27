package com.necromine.editor.model.elements;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.EnvironmentDefinitions;
import com.gadarts.necromine.model.characters.Direction;
import com.necromine.editor.model.node.Node;
import lombok.Getter;

@Getter
public class PlacedEnvObject extends PlacedModelElement {

	private static final Vector3 auxVector3_1 = new Vector3();
	private static final Vector2 auxVector2_1 = new Vector2();

	public PlacedEnvObject(final EnvironmentDefinitions definition,
						   final Node node,
						   final GameAssetsManager assetsManager,
						   final Direction selectedDirection) {
		super(node, definition, selectedDirection, assetsManager);
	}
}
