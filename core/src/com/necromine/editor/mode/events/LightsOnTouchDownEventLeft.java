package com.necromine.editor.mode.events;

import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.map.MapNodeData;
import com.necromine.editor.actions.processes.MappingProcess;
import com.necromine.editor.handlers.SelectionHandler;
import com.necromine.editor.handlers.action.ActionsHandler;

import java.util.Set;

public class LightsOnTouchDownEventLeft implements OnTouchDownLeftEvent {
	@Override
	public boolean run(final MappingProcess<? extends MappingProcess.FinishProcessParameters> currentProcess,
					   final ActionsHandler actionsHandler,
					   final GameAssetsManager assetsManager,
					   final Set<MapNodeData> initializedTiles, SelectionHandler selectionHandler) {
		actionsHandler.placeLight(assetsManager);
		return true;
	}
}
