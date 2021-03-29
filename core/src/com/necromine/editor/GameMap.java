package com.necromine.editor;

import com.gadarts.necromine.model.MapNodeData;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter
@Setter
public class GameMap {
	private float ambientLight;

	private MapNodeData[][] nodes;

	public GameMap(final Dimension dimension) {
		nodes = new MapNodeData[dimension.width][dimension.height];
	}
}
