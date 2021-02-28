package com.necromine.editor.model.node;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.gadarts.necromine.assets.Assets;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class Wall {
	private final ModelInstance modelInstance;

	@Setter
	private Assets.FloorsTextures definition;
}
