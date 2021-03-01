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


	public NodeWallsDefinitions(final MapNode mapNode) {
		Wall eastWall = mapNode.getEastWall();
		this.east = eastWall != null ? eastWall.getDefinition() : null;
		Wall southWall = mapNode.getSouthWall();
		this.south = southWall != null ? southWall.getDefinition() : null;
		Wall westWall = mapNode.getWestWall();
		this.west = westWall != null ? westWall.getDefinition() : null;
		Wall northWall = mapNode.getNorthWall();
		this.north = northWall != null ? northWall.getDefinition() : null;
	}
}
