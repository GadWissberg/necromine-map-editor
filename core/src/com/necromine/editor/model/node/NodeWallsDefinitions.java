package com.necromine.editor.model.node;

import com.gadarts.necromine.model.MapNodeData;
import com.gadarts.necromine.model.Wall;
import lombok.Getter;

@Getter
public class NodeWallsDefinitions {
	private final WallDefinition east;
	private final WallDefinition south;
	private final WallDefinition west;
	private final WallDefinition north;


	public NodeWallsDefinitions(final MapNodeData mapNodeData) {
		Wall eastWall = mapNodeData.getEastWall();
		this.east = new WallDefinition(eastWall != null ? eastWall.getDefinition() : null, 1F);
		Wall southWall = mapNodeData.getSouthWall();
		this.south = new WallDefinition(southWall != null ? southWall.getDefinition() : null, 1F);
		Wall westWall = mapNodeData.getWestWall();
		this.west = new WallDefinition(westWall != null ? westWall.getDefinition() : null, 1F);
		Wall northWall = mapNodeData.getNorthWall();
		this.north = new WallDefinition(northWall != null ? northWall.getDefinition() : null, 1F);
	}

	public NodeWallsDefinitions(final WallDefinition east,
								final WallDefinition south,
								final WallDefinition west,
								final WallDefinition north) {
		this.east = new WallDefinition(east.getTexture(), east.getVScale());
		this.south = new WallDefinition(south.getTexture(), south.getVScale());
		this.west = new WallDefinition(west.getTexture(), west.getVScale());
		this.north = new WallDefinition(north.getTexture(), north.getVScale());
	}
}
