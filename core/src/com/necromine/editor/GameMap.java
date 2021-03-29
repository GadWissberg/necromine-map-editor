package com.necromine.editor;

import com.gadarts.necromine.model.MapNodeData;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.stream.IntStream;

@Getter
@Setter
public class GameMap {
	private float ambientLight;

	private MapNodeData[][] nodes;

	public GameMap(final Dimension dimension) {
		nodes = new MapNodeData[dimension.height][dimension.width];
	}

	public void resetSize(final Dimension dimension) {
		MapNodeData[][] newNodes = new MapNodeData[dimension.height][dimension.width];
		int minWidth = Math.min(newNodes[0].length, nodes[0].length);
		int minDepth = Math.min(newNodes.length, nodes.length);
		IntStream.range(0, minDepth).forEach(row ->
				IntStream.range(0, minWidth).forEach(col -> newNodes[row][col] = nodes[row][col]));
		nodes = newNodes;
	}
}
