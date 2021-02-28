package com.necromine.editor;

import com.necromine.editor.model.node.NodeWallsDefinitions;

public interface MapManagerEventsSubscriber {
	void onTileSelectedUsingWallTilingTool(int row, int col, NodeWallsDefinitions definitions);
}
