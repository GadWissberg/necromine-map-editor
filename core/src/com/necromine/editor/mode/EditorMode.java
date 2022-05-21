package com.necromine.editor.mode;

import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.map.MapNodeData;
import com.necromine.editor.actions.processes.MappingProcess;
import com.necromine.editor.handlers.SelectionHandler;
import com.necromine.editor.handlers.action.ActionsHandler;
import com.necromine.editor.mode.tools.EditorTool;

import java.util.Set;

public interface EditorMode {
	int ordinal( );

	void onTouchDownLeft(MappingProcess<? extends MappingProcess.FinishProcessParameters> currentProcess,
						 ActionsHandler tool,
						 GameAssetsManager actionsHandler,
						 Set<MapNodeData> initializedTiles, SelectionHandler selectionHandler);

	String getDisplayName( );

	EditorTool[] getTools( );

	ModeType getType( );

	String name( );
}
