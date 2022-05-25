package com.necromine.editor.model.node;

import com.gadarts.necromine.assets.Assets;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WallDefinition {
	private Assets.SurfaceTextures texture;
	private Float vScale;
	private Float horizontalOffset;
	private Float verticalOffset;

}
