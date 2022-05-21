package com.necromine.editor.mode.events;

import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.map.MapNodeData;
import com.necromine.editor.MapRendererImpl;
import com.necromine.editor.actions.processes.MappingProcess;
import com.necromine.editor.handlers.SelectionHandler;
import com.necromine.editor.handlers.action.ActionsHandler;
import com.necromine.editor.mode.tools.EditorTool;
import com.necromine.editor.mode.tools.ElementTools;

import java.util.Set;

public class EnvOnTouchDownEventLeft implements OnTouchDownLeftEvent {
	@Override
	public boolean run(MappingProcess<? extends MappingProcess.FinishProcessParameters> currentProcess,
					   ActionsHandler actionsHandler,
					   GameAssetsManager assetsManager,
					   Set<MapNodeData> initializedTiles,
					   SelectionHandler selectionHandler) {
		EditorTool tool = MapRendererImpl.getTool();
		if (tool == ElementTools.BRUSH && selectionHandler.getSelectedElement() != null) {
			actionsHandler.placeEnvObject(assetsManager);
		} else if (tool == ElementTools.DEFINE) {
			actionsHandler.defineSelectedEnvObject();
		}
		return true;
	}
}
