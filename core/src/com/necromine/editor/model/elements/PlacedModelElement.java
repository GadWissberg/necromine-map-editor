package com.necromine.editor.model.elements;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.Coords;
import com.gadarts.necromine.model.ModelElementDefinition;
import com.gadarts.necromine.model.characters.Direction;
import com.gadarts.necromine.model.map.MapNodeData;
import lombok.Getter;

import static com.gadarts.necromine.model.characters.Direction.SOUTH;

@Getter
public abstract class PlacedModelElement extends PlacedElement {
	protected final ModelInstance modelInstance;

	public PlacedModelElement(final PlacedModelElementParameters params, final GameAssetsManager assetsManager) {
		super(params);
		this.modelInstance = new ModelInstance(assetsManager.getModel(params.getModelDefinition().getModelDefinition()));
		MapNodeData node = params.getNode();
		Coords coords = node.getCoords();
		modelInstance.transform.setTranslation(coords.getCol(), node.getHeight() + params.getHeight(), coords.getRow());
	}

	@Getter
	public static class PlacedModelElementParameters extends PlacedElementParameters {

		private final ModelElementDefinition modelDefinition;

		public PlacedModelElementParameters(final ModelElementDefinition definition,
											final Direction facingDirection,
											final MapNodeData node,
											final float height) {
			super(definition, facingDirection, node, height);
			this.modelDefinition = definition;
		}

		public PlacedModelElementParameters(final ModelElementDefinition definition,
											final MapNodeData node,
											final float height) {
			this(definition, SOUTH, node, height);
		}

		public PlacedModelElementParameters(final PlacedElementParameters params) {
			this((ModelElementDefinition) params.getDefinition(), params.getFacingDirection(), params.getNode(), params.getHeight());
		}
	}
}
