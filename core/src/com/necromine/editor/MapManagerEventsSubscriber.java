package com.necromine.editor;

import com.necromine.editor.actions.ActionAnswer;
import com.necromine.editor.model.elements.PlacedElement;
import com.necromine.editor.model.node.Node;
import com.necromine.editor.model.node.NodeWallsDefinitions;

import java.util.List;

public interface MapManagerEventsSubscriber {
    void onTileSelectedUsingWallTilingTool(int row, int col, NodeWallsDefinitions definitions);

    void onTilesSelectedForLifting(int srcRow, int srcCol, int dstRow, int dstCol);

	void nodeSelectedToSelectEnvObjectsInIt(Node node, List<? extends PlacedElement> elementsInTheNode, ActionAnswer answer);
}
