package com.necromine.editor;

import com.gadarts.necromine.model.MapNodeData;
import com.gadarts.necromine.model.MapNodesTypes;
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
		IntStream.range(0, dimension.height).forEach(row ->
				IntStream.range(0, dimension.width).forEach(col -> nodes[row][col] = new MapNodeData(row, col, MapNodesTypes.PASSABLE_NODE)));

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
