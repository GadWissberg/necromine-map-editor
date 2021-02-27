package com.necromine.editor.model.node;

import com.gadarts.necromine.assets.Assets;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NodeWallsDefinitions {
	private final Assets.FloorsTextures east;
	private final Assets.FloorsTextures south;
	private final Assets.FloorsTextures west;
	private final Assets.FloorsTextures north;

}
