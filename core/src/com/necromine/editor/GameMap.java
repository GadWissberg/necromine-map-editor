package com.necromine.editor;

import lombok.Getter;
import lombok.Setter;

import static com.necromine.editor.NecromineMapEditor.LEVEL_SIZE;
@Getter
@Setter
public class GameMap {
	private MapNode[][] tiles = new MapNode[LEVEL_SIZE][LEVEL_SIZE];
}
