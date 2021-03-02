package com.necromine.editor;

import com.gadarts.necromine.model.MapNodeData;
import lombok.Getter;
import lombok.Setter;

import static com.necromine.editor.MapEditor.LEVEL_SIZE;

@Getter
@Setter
public class GameMap {
	private MapNodeData[][] nodes = new MapNodeData[LEVEL_SIZE][LEVEL_SIZE];
}
