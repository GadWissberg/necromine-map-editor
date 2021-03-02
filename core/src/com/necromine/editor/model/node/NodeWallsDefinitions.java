package com.necromine.editor.model.node;

import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.model.MapNodeData;
import com.gadarts.necromine.model.Wall;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NodeWallsDefinitions {
	private final Assets.FloorsTextures east;
	private final Assets.FloorsTextures south;
	private final Assets.FloorsTextures west;
	private final Assets.FloorsTextures north;


	public NodeWallsDefinitions(final MapNodeData mapNodeData) {
		Wall eastWall = mapNodeData.getEastWall();
		this.east = eastWall != null ? eastWall.getDefinition() : null;
		Wall southWall = mapNodeData.getSouthWall();
		this.south = southWall != null ? southWall.getDefinition() : null;
		Wall westWall = mapNodeData.getWestWall();
		this.west = westWall != null ? westWall.getDefinition() : null;
		Wall northWall = mapNodeData.getNorthWall();
		this.north = northWall != null ? northWall.getDefinition() : null;
	}
}
