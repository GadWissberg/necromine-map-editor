package com.necromine.editor;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.gadarts.necromine.Assets;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Tile {
	private Assets.FloorsTextures textureDefinition;
	private ModelInstance modelInstance;
}
