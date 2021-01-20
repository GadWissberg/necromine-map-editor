package com.necromine.editor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.gadarts.necromine.Assets;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Tile {
	private final int col;
	private final int row;
	private final ModelInstance modelInstance;

	@Setter
	private Assets.FloorsTextures textureDefinition;

	public Tile(final Model tileModel, final int row, final int col) {
		this.modelInstance = new ModelInstance(tileModel);
		Material material = modelInstance.materials.get(0);
		material.remove(ColorAttribute.Diffuse);
		material.set(TextureAttribute.createDiffuse((Texture) null));
		modelInstance.transform.setTranslation(col,0,row);
		this.row = row;
		this.col = col;
	}
}
