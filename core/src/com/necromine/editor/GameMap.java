package com.necromine.editor;

import com.necromine.editor.model.node.MapNode;
import lombok.Getter;
import lombok.Setter;

import static com.necromine.editor.MapEditor.LEVEL_SIZE;

@Getter
@Setter
public class GameMap {
	private MapNode[][] tiles = new MapNode[LEVEL_SIZE][LEVEL_SIZE];
}
