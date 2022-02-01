package com.necromine.editor;

import com.necromine.editor.actions.ActionAnswer;
import com.necromine.editor.model.elements.PlacedElement;
import com.necromine.editor.model.elements.PlacedEnvObject;
import com.necromine.editor.model.node.FlatNode;

import java.util.List;

public interface MapManagerEventsSubscriber {
	void onTileSelectedUsingWallTilingTool(FlatNode src, FlatNode dst);

	void onTilesSelectedForLifting(int srcRow, int srcCol, int dstRow, int dstCol);

	void onNodeSelectedToSelectPlacedObjectsInIt(List<? extends PlacedElement> elementsInTheNode,
												 ActionAnswer<PlacedElement> answer);

	void onSelectedEnvObjectToDefine(PlacedEnvObject data);

	void editorIsReady( );
}
