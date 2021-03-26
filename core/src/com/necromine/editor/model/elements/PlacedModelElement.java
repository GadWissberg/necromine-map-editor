package com.necromine.editor.model.elements;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.ModelElementDefinition;
import com.gadarts.necromine.model.characters.Direction;
import com.necromine.editor.model.node.Node;
import lombok.Getter;

import static com.gadarts.necromine.model.characters.Direction.SOUTH;

@Getter
public abstract class PlacedModelElement extends PlacedElement {
	protected final ModelInstance modelInstance;

	public PlacedModelElement(final PlacedModelElementParameters params, final GameAssetsManager assetsManager) {
		super(params);
		this.modelInstance = new ModelInstance(assetsManager.getModel(params.getModelDefinition().getModelDefinition()));
		Node node = params.getNode();
		modelInstance.transform.setTranslation(node.getCol(), params.getHeight(), node.getRow());
	}

	@Getter
	public static class PlacedModelElementParameters extends PlacedElementParameters {

		private final ModelElementDefinition modelDefinition;

		public PlacedModelElementParameters(final ModelElementDefinition definition,
											final Direction facingDirection,
											final Node node,
											final float height) {
			super(definition, facingDirection, node, height);
			this.modelDefinition = definition;
		}

		public PlacedModelElementParameters(final ModelElementDefinition definition,
											final Node node,
											final float height) {
			this(definition, SOUTH, node, height);
		}

		public PlacedModelElementParameters(PlacedElementParameters params) {
			this((ModelElementDefinition) params.getDefinition(), params.getFacingDirection(), params.getNode(), params.getHeight());
		}
	}
}
