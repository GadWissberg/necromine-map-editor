package com.necromine.editor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.model.MapNodesTypes;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MapNode {

	@Setter(AccessLevel.NONE)
	private final int col;

	@Setter(AccessLevel.NONE)
	private final int row;

	private ModelInstance modelInstance;
	private MapNodesTypes mapNodeType;
	private Assets.FloorsTextures textureDefinition;

	public MapNode(final int row, final int col, final MapNodesTypes type) {
		this(null, row, col, type);
	}

	public MapNode(final Model tileModel, final int row, final int col, final MapNodesTypes type) {
		if (tileModel != null) {
			initializeModelInstance(tileModel, row, col);
		}
		this.mapNodeType = type;
		this.row = row;
		this.col = col;
	}

	private void initializeModelInstance(final Model tileModel, final int row, final int col) {
		this.modelInstance = new ModelInstance(tileModel);
		Material material = modelInstance.materials.get(0);
		material.remove(ColorAttribute.Diffuse);
		material.set(TextureAttribute.createDiffuse((Texture) null));
		modelInstance.transform.setTranslation(col, 0, row);
	}
}