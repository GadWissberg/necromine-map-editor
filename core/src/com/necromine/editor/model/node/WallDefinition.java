package com.necromine.editor.model.node;

import com.gadarts.necromine.assets.Assets;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class WallDefinition {
	private final Assets.FloorsTextures texture;
	private final Float vScale;
}
