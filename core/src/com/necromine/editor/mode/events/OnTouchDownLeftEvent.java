package com.necromine.editor.mode.events;

import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.map.MapNodeData;
import com.necromine.editor.actions.processes.MappingProcess;
import com.necromine.editor.handlers.SelectionHandler;
import com.necromine.editor.handlers.action.ActionsHandler;

import java.util.Set;

public interface OnTouchDownLeftEvent {

	boolean run(MappingProcess<? extends MappingProcess.FinishProcessParameters> currentProcess,
				ActionsHandler actionsHandler,
				GameAssetsManager assetsManager,
				Set<MapNodeData> initializedTiles,
				SelectionHandler selectionHandler);
}
